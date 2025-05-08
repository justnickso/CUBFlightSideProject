package tw.nick.cubflying.api.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import tw.nick.cubflying.data.RatesInfo

@JsonClass(generateAdapter = true)
data class LatestExchangeRateResponse(
    @Json(name = "data")
    val ratesData: Map<String, Double>
)


fun LatestExchangeRateResponse.toInfo(): List<RatesInfo> {
    val list = mutableListOf<RatesInfo>()
    for(data in this.ratesData){
        list.add(RatesInfo(
            currency = data.key,
            rate = data.value
        ))
    }
    return list
}