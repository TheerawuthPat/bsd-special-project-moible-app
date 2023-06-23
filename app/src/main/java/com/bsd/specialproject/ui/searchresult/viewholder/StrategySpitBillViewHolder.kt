package com.bsd.specialproject.ui.searchresult.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bsd.specialproject.databinding.ItemStrategySpitBillViewBinding
import com.bsd.specialproject.ui.searchresult.adapter.click.PromotionClick
import com.bsd.specialproject.ui.searchresult.model.StrategyCreditCardModel

class StrategySpitBillViewHolder(
    val binding: ItemStrategySpitBillViewBinding,
    val onClicked: ((PromotionClick) -> Unit)? = null
) : RecyclerView.ViewHolder(binding.root) {

    private val viewContext by lazy {
        binding.root.context
    }

    companion object {
        fun from(
            parent: ViewGroup,
            onClicked: ((PromotionClick) -> Unit)? = null
        ): StrategySpitBillViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemStrategySpitBillViewBinding.inflate(layoutInflater, parent, false)
            return StrategySpitBillViewHolder(binding, onClicked)
        }
    }

    fun bind(item: StrategyCreditCardModel) {
        with(binding) {

        }
    }
}
