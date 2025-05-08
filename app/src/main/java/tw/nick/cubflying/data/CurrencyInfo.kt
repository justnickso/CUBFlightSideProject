package tw.nick.cubflying.data

import com.squareup.moshi.Json

data class CurrencyInfo(
    val code: String,
    val decimalDigits: Int,
    val name: String,
    val namePlural: String,
    val rounding: Int,
    val symbol: String,
    val symbolNative: String
)