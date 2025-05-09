package tw.nick.cubflying.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.get
import tw.nick.cubflying.api.CurrencyApiService
import tw.nick.cubflying.api.FlyingApiService
import tw.nick.cubflying.api.response.CurrencyResponse
import tw.nick.cubflying.api.response.FlyingResponse
import tw.nick.cubflying.api.response.LatestExchangeRateResponse
import tw.nick.cubflying.api.retrofit.ApiResponse

class MainViewModel(
    private val apiService: FlyingApiService,
    private val currencyApiService: CurrencyApiService
) : ViewModel() {

    private val _flyingInfoFlow = MutableSharedFlow<FlyingResponse>()
    val flyingInfoFlow = _flyingInfoFlow
    private val _currencyFlow = MutableSharedFlow<CurrencyResponse>()
    val currencyFlow = _currencyFlow
    private val _errorFlow = MutableSharedFlow<String>()
    val errorFlow = _errorFlow
    private val _currencyRateFlow = MutableSharedFlow<LatestExchangeRateResponse>()
    val currencyRateFlow = _currencyRateFlow


    suspend fun getFlyingInfo(): FlyingResponse? = withContext(Dispatchers.IO) {
        when (val response = apiService.getFlyingInfo()) {
            is ApiResponse.Success -> response.data
            is ApiResponse.Failure,
            is ApiResponse.NetworkDisconnected -> {
                return@withContext null
            }
        }
    }

    fun getFlyingInfoFlow() {
        viewModelScope.launch {
            when (val response = apiService.getFlyingInfo()) {
                is ApiResponse.Success -> _flyingInfoFlow.emit(response.data)
                is ApiResponse.Failure -> _errorFlow.emit("Failure")
                is ApiResponse.NetworkDisconnected -> _errorFlow.emit("Network Disconnected")
            }
        }
    }

//    suspend fun getCurrency() : CurrencyResponse? = withContext(Dispatchers.IO) {
//        when(val response = currencyApiService.getCurrency()) {
//            is ApiResponse.Success -> response.data
//            is ApiResponse.Failure ,
//            is ApiResponse.NetworkDisconnected -> {
//                return@withContext null
//            }
//        }
//    }

    fun getCurrencyFlow() {
        viewModelScope.launch {
            when (val response = currencyApiService.getCurrency()) {
                is ApiResponse.Success -> _currencyFlow.emit(response.data)
                is ApiResponse.Failure -> _errorFlow.emit("Failure")
                is ApiResponse.NetworkDisconnected -> _errorFlow.emit("Network Disconnected")
            }
        }
    }

    fun getCurrencyRateFlow(baseCurrency: String? = null) {
        viewModelScope.launch {
            when (val response =
                currencyApiService.getLatestExchangeRate(baseCurrency = baseCurrency)) {
                is ApiResponse.Success -> _currencyRateFlow.emit(response.data)
                is ApiResponse.Failure -> _errorFlow.emit("Failure")
                is ApiResponse.NetworkDisconnected -> _errorFlow.emit("Network Disconnected")
            }
        }
    }

}