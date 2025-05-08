package tw.nick.cubflying.ui.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import tw.nick.cubflying.R
import tw.nick.cubflying.data.FlyingInfo
import tw.nick.cubflying.databinding.ItemFlyingInfoBinding

class FlyingListItemViewHolder(private val binding: ItemFlyingInfoBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(flyingInfo: FlyingInfo) {
        with(binding) {
            tvAirlineName.text = flyingInfo.airLineName
            tvAirlineCode.text = flyingInfo.airLineCode
            tvStatus.text = flyingInfo.airFlyStatus
            val boardingGate = flyingInfo.airBoardingGate
            if (boardingGate.isNotBlank()) {
                tvAirBoardingGate.text =
                    binding.root.context.getString(R.string.boarding_gate) + boardingGate
            }
            tvAirlineNum.text = flyingInfo.airLineNum
            tvAirportName.text = flyingInfo.airportName
            tvRealTime.text = flyingInfo.realTime
            tvDelayCause.text = flyingInfo.airFlyDelayCause
            tvAirplaneType.text = flyingInfo.airPlaneType
        }
    }
}