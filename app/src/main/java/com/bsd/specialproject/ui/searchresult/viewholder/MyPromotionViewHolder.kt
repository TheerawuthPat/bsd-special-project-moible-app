package com.bsd.specialproject.ui.searchresult.viewholder

import android.view.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bsd.specialproject.R
import com.bsd.specialproject.databinding.ItemMyPromotionViewBinding
import com.bsd.specialproject.ui.searchresult.model.MyPromotionModel
import com.bsd.specialproject.utils.toDefaultValue

class MyPromotionViewHolder(
    val binding: ItemMyPromotionViewBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val viewContext by lazy {
        binding.root.context
    }

    companion object {
        fun from(
            parent: ViewGroup
        ): MyPromotionViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemMyPromotionViewBinding.inflate(layoutInflater, parent, false)
            return MyPromotionViewHolder(binding)
        }
    }

    fun bind(item: MyPromotionModel) {
        with(binding) {
            tvPromotionName.text = item.promotionName

            tvAccumulateSpending.text = viewContext.getString(
                R.string.accumulate_spending,
                item.accumulateSpend.toString()
            )
            tvAccumulateCashback.text = viewContext.getString(
                R.string.accumulate_cashback,
                item.cashbackEarnedBath.toString()
            )

            tvMoreAccumulateSpending.apply {
                visibility = if(item.moreAccumulateSpend != 0) {
                    View.VISIBLE
                } else {
                    View.INVISIBLE
                }
                text =
                    viewContext.getString(
                        R.string.more_accumulate_spending,
                        item.moreAccumulateSpend.toString()
                    )
            }
            tvMoreCashbackEarned.text =
                viewContext.getString(
                    R.string.more_cashback_earned,
                    item.moreCashbackPercent.toString()
                )

            tvPromotionRemainingDate.text =
                viewContext.getString(R.string.promotion_expired_date, item.remainingDate)
            tvSavedPromotionDate.text =
                viewContext.getString(
                    R.string.saved_promotion_date,
                    item.savedDate.toDefaultValue(),
                    item.cardSelectedName.toDefaultValue()
                )
        }
    }
}
