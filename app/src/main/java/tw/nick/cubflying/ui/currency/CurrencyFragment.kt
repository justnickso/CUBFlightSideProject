package tw.nick.cubflying.ui.currency

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.startForegroundService
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import tw.nick.cubflying.api.response.toInfo
import tw.nick.cubflying.databinding.FragmentCurrencyBinding
import tw.nick.cubflying.service.CUBFlyingService
import tw.nick.cubflying.ui.adapter.CurrencyRateListAdapter
import tw.nick.cubflying.viewmodel.MainViewModel
import androidx.core.net.toUri
import org.koin.android.ext.android.inject
import tw.nick.cubflying.R
import tw.nick.cubflying.data.RatesInfo
import tw.nick.cubflying.data.datastore.CurrencyDataStore
import tw.nick.cubflying.service.floating.FloatingCommand
import tw.nick.cubflying.service.floating.FloatingEventBus
import tw.nick.cubflying.ui.adapter.OnItemSelectedListener

class CurrencyFragment : Fragment(), OnItemSelectedListener {

    private var _binding: FragmentCurrencyBinding? = null

    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by activityViewModel()
    private val currencyRateListAdapter = CurrencyRateListAdapter(this@CurrencyFragment)
    private var selectedCurrency = "USD"
    private var selectedRatesInfo: RatesInfo? = null
    private val floatingEventBus: FloatingEventBus by inject()
    private val currencyDataStore: CurrencyDataStore by inject()

    private val overlayPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ ->
            val isGranted = Settings.canDrawOverlays(this.activity)
            if (isGranted) {
                lifecycleScope.launch {
                    context?.let {
                        val serviceIntent = Intent(it, CUBFlyingService::class.java)
                        startForegroundService(it, serviceIntent)
                    }
                }
            } else {
                Toast.makeText(
                    context,
                    getString(R.string.overlay_permission_not_granted),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrencyBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
        initCollection()
        initView()
        mainViewModel.getCurrencyRateFlow()
    }

    private fun checkPermission() {
        context?.let {
            if (Settings.canDrawOverlays(context)) {
                val serviceIntent = Intent(it, CUBFlyingService::class.java)
                startForegroundService(it, serviceIntent)
            } else {
                permissionSetting()
            }
        }
    }

    private fun permissionSetting() {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            "package:${this@CurrencyFragment.activity?.packageName}".toUri()
        )
        overlayPermissionLauncher.launch(intent)
    }

    private fun initView() {
        binding.rvCurrencyRate.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = currencyRateListAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initCollection() {
        lifecycleScope.launch(Dispatchers.IO) {
            mainViewModel.currencyRateFlow.collect { response ->
                withContext(Dispatchers.Main) {
                    currencyRateListAdapter.submitList(response.toInfo())
                    setSpinner(response.toInfo())
                }
            }
        }

        lifecycleScope.launch {
            mainViewModel.currencyName.collect{
                selectedCurrency = it
            }
        }
    }

    override fun onItemSelected(ratesInfo: RatesInfo) {
        binding.tvExchangeCurrency.text = ratesInfo.currency
        if (Settings.canDrawOverlays(this.activity)) {
            selectedRatesInfo = ratesInfo
            floatingEventBus.send(
                FloatingCommand.ShowFloatingWindow(
                    selectedCurrency,
                    ratesInfo
                )
            )
        } else {
            permissionSetting()
        }
    }

    private fun setSpinner(rates: List<RatesInfo>) {
        binding.spinnerCurrency.apply {
            val currencyNames = rates.map { it.currency }

            val spinnerAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                currencyNames
            ).also {
                it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }

            adapter = spinnerAdapter

            // 預設選擇
            val defaultIndex = currencyNames.indexOf(selectedCurrency)
            if (defaultIndex != -1) {
                setSelection(defaultIndex)
            }

            selectedRatesInfo?.let { selectedRate ->
                //因為要切換幣別,所以要重新計算
                val ratesInfo = rates.first { it.currency == selectedRate.currency }
                floatingEventBus.send(
                    FloatingCommand.ShowFloatingWindow(
                        selectedCurrency, ratesInfo
                    )
                )
            }

            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    val spinnerSelectedCurrency = rates[position]
                    if (selectedCurrency != spinnerSelectedCurrency.currency) {
                        selectedCurrency = spinnerSelectedCurrency.currency
                        lifecycleScope.launch(Dispatchers.IO) {
                            currencyDataStore.setCurrency(spinnerSelectedCurrency.currency, spinnerSelectedCurrency.rate)
                        }
                        setSelection(position)
                        mainViewModel.getCurrencyRateFlow(rates[position].currency)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
        }
    }

}