package tw.nick.cubflying.api

import retrofit2.http.GET
import retrofit2.http.Header
import tw.nick.cubflying.BuildConfig
import tw.nick.cubflying.api.response.CurrencyResponse
import tw.nick.cubflying.api.response.LatestExchangeRateResponse
import tw.nick.cubflying.api.retrofit.ApiResponse

interface CurrencyApiService {

    @GET("/v1/currencies")
    suspend fun getCurrency(@Header("apikey") apiKey: String = BuildConfig.CURRECNY_API_KEY): ApiResponse<CurrencyResponse>

    @GET("/v1/latest")
    suspend fun getLatestExchangeRate(@Header("apikey") apiKey: String = BuildConfig.CURRECNY_API_KEY): ApiResponse<LatestExchangeRateResponse>
}