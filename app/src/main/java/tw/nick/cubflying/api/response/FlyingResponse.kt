package tw.nick.cubflying.api.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import tw.nick.cubflying.data.FlyingInfo
import tw.nick.cubflying.ui.enums.FlyingStatus

@JsonClass(generateAdapter = true)
data class FlyingResponse(
    @Json(name = "InstantSchedule")
    val instantSchedule: List<InstantSchedule>
) {
    @JsonClass(generateAdapter = true)
    data class InstantSchedule(
        val airBoardingGate: String = "",
        val airFlyDelayCause: String = "",
        val airFlyStatus: String = "",
        val airLineCode: String = "",
        val airLineLogo: String = "",
        val airLineName: String = "",
        val airLineNum: String = "",
        val airLineUrl: String = "",
        val airPlaneType: String = "",
        val expectTime: String = "",
        val realTime: String = "",
        val upAirportCode: String = "",
        val upAirportName: String = "",
        val goalAirportCode: String = "",
        val goalAirportName: String = ""
    )
}

fun FlyingResponse.toInfo(status: FlyingStatus): List<FlyingInfo> {
    val list = mutableListOf<FlyingInfo>()
    for (data in this.instantSchedule) {
        list.add(
            FlyingInfo(
                airBoardingGate = data.airBoardingGate,
                airFlyDelayCause = data.airFlyDelayCause,
                airFlyStatus = data.airFlyStatus,
                airLineCode = data.airLineCode,
                airLineLogo = data.airLineLogo,
                airLineName = data.airLineName,
                airLineNum = data.airLineNum,
                airLineUrl = data.airLineUrl,
                airPlaneType = data.airPlaneType,
                expectTime = data.expectTime,
                realTime = data.realTime,
                airportName = when(status){
                    FlyingStatus.DEPARTURE ->data.goalAirportName
                    FlyingStatus.ARRIVAL -> data.upAirportName
                },
                upAirportCode = when(status){
                    FlyingStatus.DEPARTURE ->data.goalAirportCode
                    FlyingStatus.ARRIVAL -> data.upAirportCode
                }
            )
        )
    }
    return list
}
