package com.bsd.specialproject.ui.searchresult.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.bsd.specialproject.R
import com.bsd.specialproject.ui.addcreditcard.adapter.click.CreditCardClick
import com.bsd.specialproject.ui.home.viewholder.EmptyViewHolder
import com.bsd.specialproject.ui.searchresult.adapter.click.PromotionClick
import com.bsd.specialproject.ui.searchresult.model.ForYouPromotionModel
import com.bsd.specialproject.ui.searchresult.viewholder.ForYouPromotionViewHolder
import com.bsd.specialproject.ui.searchresult.viewholder.MyPromotionViewHolder

class ForYouPromotionAdapter(
    private val onClick: (PromotionClick) -> Unit
) : ListAdapter<ForYouPromotionModel, RecyclerView.ViewHolder>(DIFF_COMPARATOR) {

    companion object {
        const val FOR_YOU_PROMOTION_VIEW_TYPE = R.layout.item_for_you_promotion_view
        const val EMPTY_VIEW_TYPE = R.layout.item_empty_view

        val DIFF_COMPARATOR = object : DiffUtil.ItemCallback<ForYouPromotionModel>() {
            override fun areItemsTheSame(
                oldItem: ForYouPromotionModel,
                newItem: ForYouPromotionModel
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ForYouPromotionModel,
                newItem: ForYouPromotionModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            FOR_YOU_PROMOTION_VIEW_TYPE -> {
                ForYouPromotionViewHolder.from(parent, onClick)
            }
            else -> {
                EmptyViewHolder.from(parent)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let {
            when (getItemViewType(position)) {
                FOR_YOU_PROMOTION_VIEW_TYPE -> {
                    (holder as ForYouPromotionViewHolder).bind(it)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) != null) {
            FOR_YOU_PROMOTION_VIEW_TYPE
        } else {
            EMPTY_VIEW_TYPE
        }
    }
}
