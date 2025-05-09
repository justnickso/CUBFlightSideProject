package tw.nick.cubflying.ui.flying

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import tw.nick.cubflying.api.FlyingApiService
import tw.nick.cubflying.api.response.FlyingResponse
import tw.nick.cubflying.api.retrofit.ApiResponse
import tw.nick.cubflying.util.CommonUtil

class FlyingViewModel(private val apiService: FlyingApiService) : ViewModel() {
    private val _flyingInfoListFlow = MutableSharedFlow<FlyingResponse>()
    val flyingInfoListFlow = _flyingInfoListFlow
    private val _errorFlow = MutableSharedFlow<String>()
    val errorFlow = _errorFlow
    private val _showLoading = MutableSharedFlow<Boolean>()
    val showLoading = _showLoading
    private var job: Job? = null

    fun getFlyingInfoListFlow(line: Int, io: Int = CommonUtil.DOMESTIC_FLIGHTS) {
        job?.cancel()
        job = viewModelScope.launch {
            while (isActive) {
                showLoading.emit(true)
                when (val response = apiService.getFlyingInfo(line, io)) {
                    is ApiResponse.Success -> {
                        _flyingInfoListFlow.emit(response.data)
                        _showLoading.emit(false)
                    }

                    is ApiResponse.Failure -> _errorFlow.emit("Failure")
                    is ApiResponse.NetworkDisconnected -> _errorFlow.emit("Network Disconnected")
                }
                delay(CommonUtil.API_UPDATE_TIME)
            }
        }
    }


}