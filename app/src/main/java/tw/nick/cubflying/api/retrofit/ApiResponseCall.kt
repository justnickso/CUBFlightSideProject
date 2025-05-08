package tw.nick.cubflying.api.retrofit

import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Invocation
import retrofit2.Response
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ApiResponseCall(
    private val delegate: Call<Any>
) : Call<Any> by delegate {
    override fun execute(): Response<Any> {
        return try {
            parseResponse(delegate.execute())
        } catch (e: Exception) {
            Response.success(ApiResponse.Failure<Any>(e))
        }
    }

    override fun enqueue(callback: Callback<Any>) {
        delegate.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                return callback.onResponse(this@ApiResponseCall, parseResponse(response))
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                when (t) {
                    is SocketException,
                    is SocketTimeoutException,
                    is UnknownHostException -> {
                        callback.onResponse(this@ApiResponseCall, Response.success(ApiResponse.NetworkDisconnected<Any>()))
                    }
                    else -> {
                        callback.onResponse(this@ApiResponseCall, Response.success(ApiResponse.Failure<Any>(Exception(t))))
                    }
                }
            }
        })
    }

    private fun parseResponse(response: Response<Any>): Response<Any> {
        if (response.isSuccessful) {
            val body = response.body()
            return if (body == null) {
                val invocation = delegate.request().tag(Invocation::class.java)!!
                val method = invocation.method()
                val e = KotlinNullPointerException("Response from " +
                        method.declaringClass.name +
                        '.' +
                        method.name +
                        " was null but response body type was declared as non-null")
                Response.success(ApiResponse.Failure<Any>(e, response), response.raw())
            } else {
                Response.success(ApiResponse.Success(body, response), response.raw())
            }
        } else {
            return Response.success(ApiResponse.Failure<Any>(HttpException(response), response))
        }
    }
}