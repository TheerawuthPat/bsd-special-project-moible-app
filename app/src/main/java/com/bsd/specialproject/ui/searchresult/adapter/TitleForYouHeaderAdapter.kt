package com.bsd.specialproject.ui.searchresult.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.bsd.specialproject.R
import com.bsd.specialproject.ui.home.model.ViewTitleModel
import com.bsd.specialproject.ui.home.viewholder.EmptyViewHolder
import com.bsd.specialproject.ui.searchresult.adapter.click.PromotionClick
import com.bsd.specialproject.ui.searchresult.viewholder.TitleForYouHeaderViewHolder

class TitleForYouHeaderAdapter(
    private val onClicked: (PromotionClick) -> Unit
) : ListAdapter<ViewTitleModel, RecyclerView.ViewHolder>(DIFF_COMPARATOR) {

    companion object {
        const val TITLE_FORYOU_HEADER_VIEW_TYPE = R.layout.item_title_for_you_header_view
        const val EMPTY_VIEW_TYPE = R.layout.item_empty_view

        val DIFF_COMPARATOR = object : DiffUtil.ItemCallback<ViewTitleModel>() {
            override fun areItemsTheSame(
                oldItem: ViewTitleModel,
                newItem: ViewTitleModel
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ViewTitleModel,
                newItem: ViewTitleModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    var isGrantedLocation: Boolean = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TITLE_FORYOU_HEADER_VIEW_TYPE -> {
                TitleForYouHeaderViewHolder.from(parent, onClicked)
            }

            else -> {
                EmptyViewHolder.from(parent)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let {
            when (getItemViewType(position)) {
                TITLE_FORYOU_HEADER_VIEW_TYPE -> {
                    (holder as TitleForYouHeaderViewHolder).bind(it, isGrantedLocation)
                }

                else -> {}
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) != null) {
            TITLE_FORYOU_HEADER_VIEW_TYPE
        } else {
            EMPTY_VIEW_TYPE
        }
    }
}
