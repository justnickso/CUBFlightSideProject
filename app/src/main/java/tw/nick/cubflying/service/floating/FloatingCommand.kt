package tw.nick.cubflying.service.floating

import tw.nick.cubflying.data.RatesInfo

sealed class FloatingCommand {
    class ShowFloatingWindow(val currency: String, val rateInfo: RatesInfo) : FloatingCommand()
    object HideFloatingWindow : FloatingCommand()
}