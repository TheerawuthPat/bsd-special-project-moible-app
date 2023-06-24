package com.bsd.specialproject.ui.searchresult.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.bsd.specialproject.R
import com.bsd.specialproject.ui.home.viewholder.EmptyViewHolder
import com.bsd.specialproject.ui.searchresult.adapter.click.PromotionClick
import com.bsd.specialproject.ui.searchresult.model.StrategyCreditCardModel
import com.bsd.specialproject.ui.searchresult.viewholder.StrategyFullBillViewHolder
import com.bsd.specialproject.ui.searchresult.viewholder.StrategySplitBillViewHolder

class StrategyCreditCardAdapter(
    private val onClick: (PromotionClick) -> Unit
) : ListAdapter<StrategyCreditCardModel, RecyclerView.ViewHolder>(DIFF_COMPARATOR) {

    companion object {
        const val SPIT_BILL_VIEW_TYPE = R.layout.item_strategy_spit_bill_view
        const val FULL_BILL_VIEW_TYPE = R.layout.item_strategy_full_bill_view
        const val EMPTY_VIEW_TYPE = R.layout.item_empty_view

        val DIFF_COMPARATOR = object : DiffUtil.ItemCallback<StrategyCreditCardModel>() {
            override fun areItemsTheSame(
                oldItem: StrategyCreditCardModel,
                newItem: StrategyCreditCardModel
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: StrategyCreditCardModel,
                newItem: StrategyCreditCardModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    var isSpitBill: Boolean = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            SPIT_BILL_VIEW_TYPE -> {
                StrategySplitBillViewHolder.from(parent, onClick)
            }

            FULL_BILL_VIEW_TYPE -> {
                StrategyFullBillViewHolder.from(parent, onClick)
            }

            else -> {
                EmptyViewHolder.from(parent)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            SPIT_BILL_VIEW_TYPE -> {
                (holder as StrategySplitBillViewHolder).bind(getItem(position)as StrategyCreditCardModel.SplitBillModel)
            }
            FULL_BILL_VIEW_TYPE -> {
                (holder as StrategyFullBillViewHolder).bind(getItem(position) as StrategyCreditCardModel.FullBillModel)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) is StrategyCreditCardModel.SplitBillModel) {
            SPIT_BILL_VIEW_TYPE
        } else {
            FULL_BILL_VIEW_TYPE
        }
    }
}
