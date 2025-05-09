package tw.nick.cubflying.extension

import java.math.BigDecimal
import java.math.RoundingMode

fun Int.toBigDecimal(): BigDecimal = BigDecimal(this)

fun Long.toBigDecimal(): BigDecimal = BigDecimal(this)

fun Double.toBigDecimal(): BigDecimal = BigDecimal(this)

fun Int.toExchangeBigDecimal(rate: Double): BigDecimal =
    BigDecimal(rate).setScale(4, RoundingMode.HALF_UP).multiply(
        this.toBigDecimal()
    )

fun Long.toExchangeBigDecimal(rate: Double): BigDecimal =
    BigDecimal(rate).setScale(4, RoundingMode.HALF_UP).multiply(
        this.toBigDecimal()
    )

fun Double.toExchangeBigDecimal(rate: Double): BigDecimal =
    BigDecimal(rate).setScale(4, RoundingMode.HALF_UP).multiply(
        this.toBigDecimal()
    )
