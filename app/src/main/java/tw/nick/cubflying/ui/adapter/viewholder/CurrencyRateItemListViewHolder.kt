package tw.nick.cubflying.ui.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import tw.nick.cubflying.data.RatesInfo
import tw.nick.cubflying.databinding.ItemCurrencyRateInfoBinding
import tw.nick.cubflying.ui.adapter.OnItemSelectedListener
import java.math.BigDecimal
import java.math.RoundingMode

class CurrencyRateItemListViewHolder(private val binding: ItemCurrencyRateInfoBinding ,private val onItemSelectedListener: OnItemSelectedListener) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(ratesInfo: RatesInfo) {
        with(binding) {
            layoutRate.setOnClickListener{
                onItemSelectedListener.onItemSelected(ratesInfo)
            }
            tvCurrencyName.text = ratesInfo.currency
            tvRate.text = BigDecimal(ratesInfo.rate).setScale(4, RoundingMode.HALF_UP)
                .toPlainString()
        }
    }
}