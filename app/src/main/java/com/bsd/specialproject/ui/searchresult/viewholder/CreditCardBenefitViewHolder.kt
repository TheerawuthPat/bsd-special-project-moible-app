package com.bsd.specialproject.ui.searchresult.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bsd.specialproject.R
import com.bsd.specialproject.databinding.ItemCreditCardBenefitViewBinding
import com.bsd.specialproject.ui.addcreditcard.adapter.click.CreditCardClick
import com.bsd.specialproject.ui.searchresult.model.CreditCardSearchResultModel
import com.bsd.specialproject.utils.loadImage

class CreditCardBenefitViewHolder(
    val binding: ItemCreditCardBenefitViewBinding,
    val onClicked: ((CreditCardClick) -> Unit)? = null
) : RecyclerView.ViewHolder(binding.root) {

    private val viewContext by lazy {
        binding.root.context
    }

    companion object {
        fun from(
            parent: ViewGroup,
            onClicked: ((CreditCardClick) -> Unit)? = null
        ): CreditCardBenefitViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemCreditCardBenefitViewBinding.inflate(layoutInflater, parent, false)
            return CreditCardBenefitViewHolder(binding, onClicked)
        }
    }

    fun bind(item: CreditCardSearchResultModel) {
        with(binding) {
            tvCreditCardName.text = item.name
            tvCashbackEarnedCategory.text =
                viewContext.getString(
                    R.string.cashback_earned_category,
                    item.earnedCategory
                )
            tvCashbackEarnedMoney.text = viewContext.getString(
                R.string.calculate_cashback_earned,
                item.cashbackPercent,
                item.cashbackEarnedBath
            )
            tvLimitCashbackPerMonth.text = viewContext.getString(
                R.string.limit_cashback_per_month,
                item.limitCashbackPerMonth.toString()
            )
            tvEstimateSpending.text = viewContext.getString(
                R.string.estimate_spending,
                item.estimateSpending
            )
        }
    }
}
