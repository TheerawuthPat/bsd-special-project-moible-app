package com.bsd.specialproject.ui.searchresult.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bsd.specialproject.R
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

    fun bind(item: StrategyCreditCardModel.FullBillModel) {
        with(binding) {
            tvMustSpendFullBill.text = viewContext.getString(
                R.string.full_bill_must_spend,
                item.estimateSpend
            )
            tvCardOfMustSpend.text = viewContext.getString(
                R.string.full_bill_credit_card_cashback,
                item.mustCreditCardSpends.firstOrNull()?.creditCardName,
                item.mustCreditCardSpends.firstOrNull()?.cashbackEarned.toString()
            )
            tvBalanceSpend.text= viewContext.getString(
                R.string.full_bill_balance_spend,
                item.balanceSpend
            )
            item.balanceCreditCardSpends.forEachIndexed { index, balanceCreditCardSpendModel ->
                when (index) {
                    0 -> {
                        if (balanceCreditCardSpendModel.cashbackEarned != 0) {
                            tvCardOfBalanceSpendFirst.text = viewContext.getString(
                                R.string.full_bill_balance_credit_card_spend,
                                balanceCreditCardSpendModel.creditCardName,
                                balanceCreditCardSpendModel.balanceSpendForMaximumCashback.toString(),
                                balanceCreditCardSpendModel.cashbackEarned.toString()
                            )
                            tvCardOfBalanceSpendFirst.isVisible = true
                        } else {
                            tvCardOfBalanceSpendFirst.isVisible = false
                        }
                    }
                    1 -> {
                        if (balanceCreditCardSpendModel.cashbackEarned != 0) {
                            tvCardOfBalanceSpendSecond.text = viewContext.getString(
                                R.string.full_bill_balance_credit_card_spend,
                                balanceCreditCardSpendModel.creditCardName,
                                balanceCreditCardSpendModel.balanceSpendForMaximumCashback.toString(),
                                balanceCreditCardSpendModel.cashbackEarned.toString()
                            )
                            tvCardOfBalanceSpendSecond.isVisible = true
                        } else {
                            tvCardOfBalanceSpendSecond.isVisible = false
                        }
                    }
                    2 -> {
                        if (balanceCreditCardSpendModel.cashbackEarned != 0) {
                            tvCardOfBalanceSpendThird.text = viewContext.getString(
                                R.string.full_bill_balance_credit_card_spend,
                                balanceCreditCardSpendModel.creditCardName,
                                balanceCreditCardSpendModel.balanceSpendForMaximumCashback.toString(),
                                balanceCreditCardSpendModel.cashbackEarned.toString()
                            )
                            tvCardOfBalanceSpendThird.isVisible = true
                        } else {
                            tvCardOfBalanceSpendThird.isVisible = false
                        }
                    }
                }
            }
            tvTotalCashbackOfMonth.text = viewContext.getString(
                R.string.total_cashback_earned,
                item.totalCashback.toString()
            )
        }
    }
}
