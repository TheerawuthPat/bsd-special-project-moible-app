package com.bsd.specialproject.ui.home.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bsd.specialproject.R
import com.bsd.specialproject.databinding.ItemMyCreditCardStepViewBinding
import com.bsd.specialproject.ui.addcreditcard.adapter.click.CreditCardClick
import com.bsd.specialproject.ui.addcreditcard.model.CreditCardModel
import com.bsd.specialproject.utils.loadImage
import com.bsd.specialproject.utils.toDefaultValue

class MyCreditCardStepViewHolder(
    val binding: ItemMyCreditCardStepViewBinding,
    val onClicked: ((CreditCardClick) -> Unit)? = null
) : RecyclerView.ViewHolder(binding.root) {

    private val viewContext by lazy {
        binding.root.context
    }

    companion object {
        fun from(
            parent: ViewGroup,
            onClicked: ((CreditCardClick) -> Unit)? = null
        ): MyCreditCardStepViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemMyCreditCardStepViewBinding.inflate(layoutInflater, parent, false)
            return MyCreditCardStepViewHolder(binding, onClicked)
        }
    }

    fun bind(item: CreditCardModel) {
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
            tvSpendStep1.text = viewContext.getString(
                R.string.spend_step_1,
                item.cashbackConditions?.get(0)?.minSpend.toString(),
                item.cashbackConditions?.get(0)?.maxSpend.toString(),
            )
            tvSpendStep2.text = viewContext.getString(
                R.string.spend_step_2,
                item.cashbackConditions?.get(1)?.minSpend.toString(),
                item.cashbackConditions?.get(1)?.maxSpend.toString(),
            )
            tvSpendStep3.text = viewContext.getString(
                R.string.spend_step_3,
                item.cashbackConditions?.get(2)?.minSpend.toString()
            )
            tvCashbackStep1.text = viewContext.getString(
                R.string.cashback_step,
                item.cashbackConditions?.get(0)?.cashbackPerTime.toString()
            )
            tvCashbackStep2.text = viewContext.getString(
                R.string.cashback_step,
                item.cashbackConditions?.get(1)?.cashbackPerTime.toString()
            )
            tvCashbackStep3.text = viewContext.getString(
                R.string.cashback_step,
                item.cashbackConditions?.get(2)?.cashbackPerTime.toString()
            )
            tvLimitCashbackPerMonth.text =
                viewContext.getString(R.string.limit_cashback_per_month, item.limitCashbackPerMonth.toString())

        }
    }
}
