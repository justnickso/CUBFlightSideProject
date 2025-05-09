package tw.nick.cubflying.ui.flying

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieDrawable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import tw.nick.cubflying.api.response.toInfo
import tw.nick.cubflying.databinding.FragmentFlyingBinding
import tw.nick.cubflying.ui.adapter.FlyingListAdapter
import tw.nick.cubflying.ui.enums.FlyingStatus
import tw.nick.cubflying.viewmodel.MainViewModel

class FlyingFragment : Fragment() {

    private var _binding: FragmentFlyingBinding? = null

    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by activityViewModel()
    private val flyingViewModel: FlyingViewModel by inject()
    private val flyingListAdapter = FlyingListAdapter()
    private var status = FlyingStatus.ARRIVAL

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFlyingBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCollection()
        initView()
        mainViewModel.getFlyingInfoFlow()
    }

    private fun initView() {
        //default Data
        flyingViewModel.getFlyingInfoListFlow(status.value)

        binding.rvFlying.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = flyingListAdapter
        }

        binding.btnArrival.setOnClickListener {
            status = FlyingStatus.ARRIVAL
            flyingViewModel.getFlyingInfoListFlow(status.value)
        }

        binding.btnDeparture.setOnClickListener {
            status = FlyingStatus.DEPARTURE
            flyingViewModel.getFlyingInfoListFlow(status.value)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initCollection() {
        lifecycleScope.launch(Dispatchers.IO) {
            flyingViewModel.flyingInfoListFlow.collect { response ->
                withContext(Dispatchers.Main) {
                    Log.d("FlyingFragment", "flyingInfoFlow: ${response.toInfo(status)}")
                    flyingListAdapter.submitList(response.toInfo(status))
                }
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            flyingViewModel.errorFlow.collect { error ->
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            flyingViewModel.showLoading.collect { show ->
                withContext(Dispatchers.Main) {
                     if(show) {
                         binding.lottieLoading.apply {
                             visibility = View.VISIBLE
                             repeatMode = LottieDrawable.RESTART
                             repeatCount = LottieDrawable.INFINITE
                             playAnimation()
                         }
                     } else {
                         binding.lottieLoading.apply {
                             visibility = View.GONE
                             cancelAnimation()
                         }
                     }
                }
            }
        }
    }
}