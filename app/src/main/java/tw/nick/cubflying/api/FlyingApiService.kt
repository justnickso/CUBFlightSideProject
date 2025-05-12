package tw.nick.cubflying.api

import retrofit2.http.GET
import retrofit2.http.Query
import tw.nick.cubflying.api.response.FlyingResponse
import tw.nick.cubflying.api.retrofit.ApiResponse

interface FlyingApiService {
    @GET("/API/InstantSchedule.ashx")
    suspend fun getFlyingInfo(
        @Query("AirFlyLine") line: Int,
        @Query("AirFlyIO") io: Int
    ): ApiResponse<FlyingResponse>

}