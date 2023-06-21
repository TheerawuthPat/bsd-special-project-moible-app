package com.bsd.specialproject.ui.searchresult.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bsd.specialproject.R
import com.bsd.specialproject.databinding.ItemForYouPromotionViewBinding
import com.bsd.specialproject.ui.searchresult.adapter.click.PromotionClick
import com.bsd.specialproject.ui.searchresult.model.ForYouPromotionModel

class ForYouPromotionViewHolder(
    val binding: ItemForYouPromotionViewBinding,
    val onClicked: ((PromotionClick) -> Unit)? = null
) : RecyclerView.ViewHolder(binding.root) {

    private val viewContext by lazy {
        binding.root.context
    }

    companion object {
        fun from(
            parent: ViewGroup,
            onClicked: ((PromotionClick) -> Unit)? = null
        ): ForYouPromotionViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemForYouPromotionViewBinding.inflate(layoutInflater, parent, false)
            return ForYouPromotionViewHolder(binding, onClicked)
        }
    }

    fun bind(item: ForYouPromotionModel) {
        with(binding) {
            tvPromotionName.text = item.name
            if (item.distance != null) {
                tvDistance.isVisible = true
                tvDistance.text =
                    viewContext.getString(R.string.promotion_distance, item.distance.toString())
            } else {
                tvDistance.isVisible = false
            }
            tvCashbackEarnedPercent.text =
                viewContext.getString(R.string.cashback_earned_percent, item.cashbackPercent)
            tvCashbackEarnedBath.text =
                viewContext.getString(R.string.cashback_earned_to_bath, item.cashbackEarnedBath)
            ivRegisterPromotion.setOnClickListener {
                onClicked?.invoke(PromotionClick.SelectedClick(item))
            }
        }
    }
}
