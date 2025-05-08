package tw.nick.cubflying.api.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import tw.nick.cubflying.data.CurrencyInfo

@JsonClass(generateAdapter = true)
data class CurrencyResponse(
    @Json(name = "data")
    val currencyData: Map<String, CurrencyInfoData>
) {
    @JsonClass(generateAdapter = true)
    data class CurrencyInfoData(
        val code: String,
        @Json(name = "decimal_digits")
        val decimalDigits: Int,
        val name: String,
        @Json(name = "name_plural")
        val namePlural: String,
        val rounding: Int,
        val symbol: String,
        @Json(name = "symbol_native")
        val symbolNative: String
    )
}

fun CurrencyResponse.toInfo(): List<CurrencyInfo> {
    val list = mutableListOf<CurrencyInfo>()
    for(data in this.currencyData.values){
        list.add(CurrencyInfo(
            code = data.code,
            decimalDigits = data.decimalDigits,
            name = data.name,
            namePlural = data.namePlural,
            rounding = data.rounding,
            symbol = data.symbol,
            symbolNative = data.symbolNative
        ))
    }
    return list
}