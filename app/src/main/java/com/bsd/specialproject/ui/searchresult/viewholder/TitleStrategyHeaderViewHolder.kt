package com.bsd.specialproject.ui.searchresult.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bsd.specialproject.R
import com.bsd.specialproject.databinding.ItemTitleStrategyHeaderViewBinding
import com.bsd.specialproject.ui.home.model.ViewTitleModel
import com.bsd.specialproject.ui.searchresult.adapter.click.PromotionClick

class TitleStrategyHeaderViewHolder(
    private val binding: ItemTitleStrategyHeaderViewBinding,
    private val onClicked: ((PromotionClick) -> Unit)? = null
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(
            parent: ViewGroup,
            onClicked: (PromotionClick) -> Unit
        ): TitleStrategyHeaderViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemTitleStrategyHeaderViewBinding.inflate(layoutInflater, parent, false)
            return TitleStrategyHeaderViewHolder(binding, onClicked)
        }
    }

    private val viewContext by lazy {
        binding.root.context
    }

    fun bind(item: ViewTitleModel, isSpitBill: Boolean) = with(binding) {
        with(item) {
            tvTitle.apply {
                text = title
            }
            btnSpitBill.apply {
                if (isSpitBill) {
                    setBackgroundColor(viewContext.getColor(R.color.blue_light_300))
                } else {
                    alpha = 0.3f
                }
                setOnClickListener {
                    binding.btnFullBill.apply {
                        alpha = 0.3f
                    }
                    setBackgroundColor(viewContext.getColor(R.color.blue_light_300))
                    onClicked?.invoke(PromotionClick.SpitBillClick)
                    alpha = 1f
                }
            }
            btnFullBill.apply {
                if (!isSpitBill) {
                    setBackgroundColor(viewContext.getColor(R.color.blue_light_300))
                } else {
                    alpha = 0.3f
                }
                setOnClickListener {
                    binding.btnSpitBill.apply {
                        alpha = 0.3f
                    }
                    setBackgroundColor(viewContext.getColor(R.color.blue_light_300))
                    onClicked?.invoke(PromotionClick.FullBillClick)
                    alpha = 1f
                }
            }
        }
    }
}
