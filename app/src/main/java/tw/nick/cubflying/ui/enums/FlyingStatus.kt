package tw.nick.cubflying.ui.enums

enum class FlyingStatus(val value: Int) {
    DEPARTURE(1), ARRIVAL(2);

    companion object {
        fun fromInt(value: Int): FlyingStatus {
            return when (value) {
                1 -> DEPARTURE
                2 -> ARRIVAL
                else -> DEPARTURE
            }
        }
    }
}