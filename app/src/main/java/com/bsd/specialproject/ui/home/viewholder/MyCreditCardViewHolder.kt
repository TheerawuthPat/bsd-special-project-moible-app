package com.bsd.specialproject.ui.home.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bsd.specialproject.R
import com.bsd.specialproject.databinding.ItemMyCreditCardViewBinding
import com.bsd.specialproject.ui.addcreditcard.adapter.click.CreditCardClick
import com.bsd.specialproject.ui.addcreditcard.model.CreditCardResponse
import com.bsd.specialproject.utils.loadImage
import com.bsd.specialproject.utils.toDefaultValue

class MyCreditCardViewHolder(
    val binding: ItemMyCreditCardViewBinding,
    val onClicked: ((CreditCardClick) -> Unit)? = null
) : RecyclerView.ViewHolder(binding.root) {

    private val viewContext by lazy {
        binding.root.context
    }

    companion object {
        fun from(
            parent: ViewGroup,
            onClicked: ((CreditCardClick) -> Unit)? = null
        ): MyCreditCardViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemMyCreditCardViewBinding.inflate(layoutInflater, parent, false)
            return MyCreditCardViewHolder(binding, onClicked)
        }
    }

    fun bind(item: CreditCardResponse) {
        with(binding) {
            loadImage(
                url = item.imageUrl.toDefaultValue(),
                imageView = ivCreditCard,
                placeholderRes = R.drawable.placeholder_credit_card,
            )
            tvCreditCardName.text = item.name.toDefaultValue()
            tvCategoryType.text =
                if (item.categoryType?.size.toDefaultValue() >= 2) {
                    viewContext.getString(
                        R.string.two_category_type,
                        item.categoryType?.get(0).toDefaultValue(),
                        item.categoryType?.get(1).toDefaultValue()
                    )
                } else {
                    viewContext.getString(
                        R.string.category_type,
                        item.categoryType?.get(0).toDefaultValue()
                    )
                }
            tvMinSpend.text = viewContext.getString(
                R.string.min_spend,
                item.cashbackConditions?.get(0)?.minSpend.toString()
            )
            tvCashbackPerTime.text = viewContext.getString(
                R.string.cashback_percent_per_time,
                item.cashbackConditions?.get(0)?.cashbackPerTime.toString()
            )
            tvLimitCashbackPerMonth.text =
                viewContext.getString(R.string.limit_cashback_per_month, item.limitCashbackPerMonth.toString())

        }
    }
}
