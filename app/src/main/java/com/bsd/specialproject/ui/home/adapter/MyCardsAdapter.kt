package com.bsd.specialproject.ui.home.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.bsd.specialproject.R
import com.bsd.specialproject.constants.CashbackType
import com.bsd.specialproject.ui.addcreditcard.adapter.click.CreditCardClick
import com.bsd.specialproject.ui.addcreditcard.model.CreditCardModel
import com.bsd.specialproject.ui.home.viewholder.*

class MyCardsAdapter(
    private val onClick: (CreditCardClick) -> Unit
) : ListAdapter<CreditCardModel, RecyclerView.ViewHolder>(DIFF_COMPARATOR) {

    companion object {
        const val PERCENT_VIEW_TYPE = R.layout.item_credit_card_view
        const val STEP_VIEW_TYPE = R.layout.item_my_credit_card_step_view
        const val EMPTY_VIEW_TYPE = R.layout.item_empty_view

        val DIFF_COMPARATOR = object : DiffUtil.ItemCallback<CreditCardModel>() {
            override fun areItemsTheSame(
                oldItem: CreditCardModel,
                newItem: CreditCardModel
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: CreditCardModel,
                newItem: CreditCardModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            PERCENT_VIEW_TYPE -> {
                MyCreditCardViewHolder.from(parent, onClick)
            }
            STEP_VIEW_TYPE -> {
                MyCreditCardStepViewHolder.from(parent, onClick)
            }
            else -> {
                EmptyViewHolder.from(parent)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let {
            when (getItemViewType(position)) {
                PERCENT_VIEW_TYPE -> {
                    (holder as MyCreditCardViewHolder).bind(it)
                }
                STEP_VIEW_TYPE -> {
                    (holder as MyCreditCardStepViewHolder).bind(it)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).cashbackType) {
            CashbackType.PERCENT.name.lowercase() -> {
                PERCENT_VIEW_TYPE
            }
            CashbackType.STEP.name.lowercase() -> {
                STEP_VIEW_TYPE
            }
            else -> EMPTY_VIEW_TYPE
        }
    }
}
