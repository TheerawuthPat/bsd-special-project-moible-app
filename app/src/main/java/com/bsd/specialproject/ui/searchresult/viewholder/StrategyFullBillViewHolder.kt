package com.bsd.specialproject.ui.searchresult.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bsd.specialproject.databinding.ItemStrategyFullBillViewBinding
import com.bsd.specialproject.ui.searchresult.adapter.click.PromotionClick
import com.bsd.specialproject.ui.searchresult.model.StrategyCreditCardModel

class StrategyFullBillViewHolder(
    val binding: ItemStrategyFullBillViewBinding,
    val onClicked: ((PromotionClick) -> Unit)? = null
) : RecyclerView.ViewHolder(binding.root) {

    private val viewContext by lazy {
        binding.root.context
    }

    companion object {
        fun from(
            parent: ViewGroup,
            onClicked: ((PromotionClick) -> Unit)? = null
        ): StrategyFullBillViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemStrategyFullBillViewBinding.inflate(layoutInflater, parent, false)
            return StrategyFullBillViewHolder(binding, onClicked)
        }
    }

    fun bind(item: StrategyCreditCardModel) {
        with(binding) {

        }
    }
}
