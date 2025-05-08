package tw.nick.cubflying.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import tw.nick.cubflying.data.RatesInfo
import tw.nick.cubflying.databinding.ItemCurrencyRateInfoBinding
import tw.nick.cubflying.ui.adapter.viewholder.CurrencyRateItemListViewHolder

class CurrencyRateListAdapter : ListAdapter<RatesInfo, RecyclerView.ViewHolder>(diffCallback){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       return CurrencyRateItemListViewHolder(
           ItemCurrencyRateInfoBinding.inflate(
               LayoutInflater.from(parent.context),
               parent,
               false
           )
       )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CurrencyRateItemListViewHolder).onBind(getItem(position))
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<RatesInfo>() {
            override fun areItemsTheSame(
                oldItem: RatesInfo,
                newItem: RatesInfo
            ): Boolean {
                return (oldItem.currency == newItem.currency) && oldItem.rate == newItem.rate
            }

            override fun areContentsTheSame(
                oldItem: RatesInfo,
                newItem: RatesInfo
            ): Boolean {
                return (oldItem.currency == newItem.currency) && oldItem.rate == newItem.rate
            }
        }
    }

}