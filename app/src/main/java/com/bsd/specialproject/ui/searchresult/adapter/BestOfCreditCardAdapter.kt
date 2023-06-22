package com.bsd.specialproject.ui.searchresult.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.bsd.specialproject.R
import com.bsd.specialproject.ui.addcreditcard.adapter.click.CreditCardClick
import com.bsd.specialproject.ui.home.viewholder.EmptyViewHolder
import com.bsd.specialproject.ui.searchresult.model.CreditCardSearchResultModel
import com.bsd.specialproject.ui.searchresult.viewholder.BestOfCreditCardViewHolder

class BestOfCreditCardAdapter(
    private val onClick: (CreditCardClick) -> Unit
) : ListAdapter<CreditCardSearchResultModel, RecyclerView.ViewHolder>(DIFF_COMPARATOR) {

    companion object {
        const val BEST_OF_CREDIT_CARD_VIEW_TYPE = R.layout.item_best_of_credit_card_view
        const val EMPTY_VIEW_TYPE = R.layout.item_empty_view

        val DIFF_COMPARATOR = object : DiffUtil.ItemCallback<CreditCardSearchResultModel>() {
            override fun areItemsTheSame(
                oldItem: CreditCardSearchResultModel,
                newItem: CreditCardSearchResultModel
            ): Boolean {
                return oldItem.isCashbackHighest == newItem.isCashbackHighest
            }

            override fun areContentsTheSame(
                oldItem: CreditCardSearchResultModel,
                newItem: CreditCardSearchResultModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            BEST_OF_CREDIT_CARD_VIEW_TYPE -> {
                BestOfCreditCardViewHolder.from(parent, onClick)
            }
            else -> {
                EmptyViewHolder.from(parent)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let {
            when (getItemViewType(position)) {
                BEST_OF_CREDIT_CARD_VIEW_TYPE -> {
                    (holder as BestOfCreditCardViewHolder).bind(it)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) != null) {
            BEST_OF_CREDIT_CARD_VIEW_TYPE
        } else {
            EMPTY_VIEW_TYPE
        }
    }
}
