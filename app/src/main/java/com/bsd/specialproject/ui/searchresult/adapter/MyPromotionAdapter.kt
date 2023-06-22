package com.bsd.specialproject.ui.searchresult.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.bsd.specialproject.R
import com.bsd.specialproject.ui.addcreditcard.adapter.click.CreditCardClick
import com.bsd.specialproject.ui.searchresult.model.MyPromotionModel
import com.bsd.specialproject.ui.searchresult.viewholder.MyPromotionEmptyViewHolder
import com.bsd.specialproject.ui.searchresult.viewholder.MyPromotionViewHolder

class MyPromotionAdapter(
    private val onClick: (CreditCardClick) -> Unit
) : ListAdapter<MyPromotionModel, RecyclerView.ViewHolder>(DIFF_COMPARATOR) {

    companion object {
        const val MY_PROMOTION_VIEW_TYPE = R.layout.item_my_promotion_view
        const val EMPTY_VIEW_TYPE = R.layout.item_my_promotion_empty_view

        val DIFF_COMPARATOR = object : DiffUtil.ItemCallback<MyPromotionModel>() {
            override fun areItemsTheSame(
                oldItem: MyPromotionModel,
                newItem: MyPromotionModel
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: MyPromotionModel,
                newItem: MyPromotionModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MY_PROMOTION_VIEW_TYPE -> {
                MyPromotionViewHolder.from(parent)
            }
            else -> {
                MyPromotionEmptyViewHolder.from(parent)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let {
            when (getItemViewType(position)) {
                MY_PROMOTION_VIEW_TYPE -> {
                    (holder as MyPromotionViewHolder).bind(it)
                }
                EMPTY_VIEW_TYPE -> {
                    (holder as MyPromotionEmptyViewHolder)
                }
                else -> {}
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(itemCount != 0) {
            MY_PROMOTION_VIEW_TYPE
        } else {
            EMPTY_VIEW_TYPE
        }
    }
}
