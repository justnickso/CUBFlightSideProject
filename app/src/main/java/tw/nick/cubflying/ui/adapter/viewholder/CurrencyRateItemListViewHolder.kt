package tw.nick.cubflying.ui.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import tw.nick.cubflying.data.RatesInfo
import tw.nick.cubflying.databinding.ItemCurrencyRateInfoBinding
import java.math.BigDecimal
import java.math.RoundingMode

class CurrencyRateItemListViewHolder(private val binding: ItemCurrencyRateInfoBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(ratesInfo: RatesInfo) {
        with(binding) {
            tvCurrencyName.text = ratesInfo.currency
            tvRate.text = BigDecimal(ratesInfo.rate).setScale(2, RoundingMode.HALF_UP)
                .toPlainString()
        }
    }
}