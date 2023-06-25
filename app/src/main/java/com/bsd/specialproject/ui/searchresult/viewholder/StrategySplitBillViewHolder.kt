package com.bsd.specialproject.ui.searchresult.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bsd.specialproject.R
import com.bsd.specialproject.databinding.ItemStrategySpitBillViewBinding
import com.bsd.specialproject.ui.searchresult.adapter.click.PromotionClick
import com.bsd.specialproject.ui.searchresult.model.StrategyCreditCardModel

class StrategySplitBillViewHolder(
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
        ): StrategySplitBillViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemStrategySpitBillViewBinding.inflate(layoutInflater, parent, false)
            return StrategySplitBillViewHolder(binding, onClicked)
        }
    }

    fun bind(item: StrategyCreditCardModel.SplitBillModel) {
        with(binding) {
            tvMustSpendSplitBill.text = viewContext.getString(
                R.string.split_bill_must_spend,
                item.estimateSpend
            )
            item.mustCreditCardSpends.forEachIndexed { index, balanceCreditCardSpendModel ->
                when (index) {
                    0 -> {
                        if (balanceCreditCardSpendModel.cashbackEarned != 0) {
                            tvCardOfSplitBillFirst.text = viewContext.getString(
                                R.string.split_bill_credit_card_cashback,
                                balanceCreditCardSpendModel.creditCardName,
                                "${index+1}",
                                balanceCreditCardSpendModel.installmentSpend.toString(),
                                balanceCreditCardSpendModel.cashbackEarned.toString()
                            )
                            tvCardOfSplitBillFirst.isVisible = true
                        } else {
                            tvCardOfSplitBillFirst.isVisible = false
                        }
                    }

                    1 -> {
                        if (balanceCreditCardSpendModel.cashbackEarned != 0) {
                            tvCardOfSplitBillSecond.text = viewContext.getString(
                                R.string.split_bill_credit_card_cashback,
                                balanceCreditCardSpendModel.creditCardName,
                                "${index+1}",
                                balanceCreditCardSpendModel.installmentSpend.toString(),
                                balanceCreditCardSpendModel.cashbackEarned.toString()
                            )
                            tvCardOfSplitBillSecond.isVisible = true
                        } else {
                            tvCardOfSplitBillSecond.isVisible = false
                        }
                    }

                    2 -> {
                        if (balanceCreditCardSpendModel.cashbackEarned != 0) {
                            tvCardOfSplitBillThird.text = viewContext.getString(
                                R.string.split_bill_credit_card_cashback,
                                balanceCreditCardSpendModel.creditCardName,
                                "${index+1}",
                                balanceCreditCardSpendModel.installmentSpend.toString(),
                                balanceCreditCardSpendModel.cashbackEarned.toString()
                            )
                            tvCardOfSplitBillThird.isVisible = true
                        } else {
                            tvCardOfSplitBillThird.isVisible = false
                        }
                    }
                }
            }
            tvBalanceSpend.text = viewContext.getString(
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
                                balanceCreditCardSpendModel.installmentSpend.toString(),
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
                                balanceCreditCardSpendModel.installmentSpend.toString(),
                                balanceCreditCardSpendModel.cashbackEarned.toString()
                            )
                            tvCardOfBalanceSpendSecond.isVisible = true
                        } else {
                            tvCardOfBalanceSpendSecond.isVisible = false
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
