package com.bsd.specialproject.ui.addcreditcard.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bsd.specialproject.R
import com.bsd.specialproject.ui.addcreditcard.adapter.click.CreditCardClick
import com.bsd.specialproject.ui.addcreditcard.model.CreditCardResponse
import com.bsd.specialproject.ui.addcreditcard.viewholder.CreditCardViewHolder

class CreditCardAdapter(
    private val isEnableDelete: Boolean,
    private val onClick: (CreditCardClick) -> Unit
) : ListAdapter<CreditCardResponse, CreditCardViewHolder>(DIFF_COMPARATOR) {

    companion object {
        const val VIEW_TYPE = R.layout.item_credit_card_view
        val DIFF_COMPARATOR = object : DiffUtil.ItemCallback<CreditCardResponse>() {
            override fun areItemsTheSame(
                oldItem: CreditCardResponse,
                newItem: CreditCardResponse
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: CreditCardResponse,
                newItem: CreditCardResponse
            ): Boolean {
                return oldItem == newItem
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditCardViewHolder {
        return CreditCardViewHolder.from(parent, isEnableDelete, onClick)
    }

    override fun onBindViewHolder(holder: CreditCardViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun getItemViewType(position: Int) = VIEW_TYPE
}
