package tw.nick.cubflying.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import tw.nick.cubflying.data.FlyingInfo
import tw.nick.cubflying.databinding.ItemFlyingInfoBinding
import tw.nick.cubflying.ui.adapter.viewholder.FlyingListItemViewHolder

class FlyingListAdapter : ListAdapter<FlyingInfo, RecyclerView.ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FlyingListItemViewHolder(
            ItemFlyingInfoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as FlyingListItemViewHolder).onBind(getItem(position))
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<FlyingInfo>() {
            override fun areItemsTheSame(
                oldItem: FlyingInfo,
                newItem: FlyingInfo
            ): Boolean {
                return oldItem.expectTime == newItem.expectTime
            }

            override fun areContentsTheSame(
                oldItem: FlyingInfo,
                newItem: FlyingInfo
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}