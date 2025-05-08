package tw.nick.cubflying.api.retrofit

import retrofit2.Response

sealed class ApiResponse<D> {
    class Success<D>(val data: D, val retrofitResponse: Response<Any>) : ApiResponse<D>()
    class Failure<D>(val exception: Exception, val retrofitResponse: Response<Any>? = null) : ApiResponse<D>()
    class NetworkDisconnected<D> : ApiResponse<D>()
}