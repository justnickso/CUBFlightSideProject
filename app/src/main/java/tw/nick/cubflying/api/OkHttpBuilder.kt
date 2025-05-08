package tw.nick.cubflying.api

import okhttp3.OkHttpClient
import tw.nick.cubflying.api.interceptor.CustomLoggingInterceptor
import tw.nick.cubflying.BuildConfig

class OkHttpBuilder {

    fun build(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(CustomLoggingInterceptor.getLargeHttpLoggingInterceptor())
            }
        }
        return builder.build()
    }
}