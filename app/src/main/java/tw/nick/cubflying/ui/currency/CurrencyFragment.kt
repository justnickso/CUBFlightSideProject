package tw.nick.cubflying.ui.currency

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.startForegroundService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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

class CurrencyFragment : Fragment() {

    private var _binding: FragmentCurrencyBinding? = null

    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by activityViewModel()
    private val currencyRateListAdapter = CurrencyRateListAdapter()

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
//                lifecycle.addObserver(object : DefaultLifecycleObserver {
//                    override fun onResume(owner: LifecycleOwner) {
//                        toRetryPage()
//                    }
//                })
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(CurrencyViewModel::class.java)

        _binding = FragmentCurrencyBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textNotifications
//        notificationsViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCollection()
        initView()
        mainViewModel.getCurrencyRateFlow()
    }

    private fun initView() {
        binding.rvCurrencyRate.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = currencyRateListAdapter
        }

        binding.btnCalculate.setOnClickListener {
            context?.let {
                if (Settings.canDrawOverlays(context)) {
                    val serviceIntent = Intent(it, CUBFlyingService::class.java)
                    startForegroundService(it, serviceIntent)
                } else {
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        "package:${this@CurrencyFragment.activity?.packageName}".toUri()
                    )

                    overlayPermissionLauncher.launch(intent)
                }
            }
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
                }
            }
        }

    }
}