package tw.nick.cubflying.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import tw.nick.cubflying.BuildConfig
import tw.nick.cubflying.api.interceptor.CustomLoggingInterceptor
import tw.nick.cubflying.api.moshi.MoshiProvider
import tw.nick.cubflying.api.retrofit.ApiResponseCallAdapterFactory
import java.util.concurrent.TimeUnit

class RetrofitBuilder(private val baseUrl: String = BuildConfig.BASE_URL) {
    private val timeoutPeriod = 10L

    private fun createOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.apply {
            readTimeout(timeoutPeriod, TimeUnit.SECONDS)
            writeTimeout(timeoutPeriod, TimeUnit.SECONDS)
            if (BuildConfig.DEBUG) {
                addInterceptor(CustomLoggingInterceptor.getLargeHttpLoggingInterceptor())
            }
        }
        return builder.build()
    }

    // Build and provide Retrofit instance
    fun build(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(createOkHttpClient())
            .addConverterFactory(MoshiConverterFactory.create(MoshiProvider.moshiDefaultIfNull))
            .addCallAdapterFactory(ApiResponseCallAdapterFactory())
            .build()
    }
}