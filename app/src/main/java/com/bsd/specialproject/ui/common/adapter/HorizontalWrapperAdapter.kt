package com.bsd.specialproject.ui.common.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bsd.specialproject.R
import com.bsd.specialproject.ui.common.BaseAdapter
import com.bsd.specialproject.ui.common.viewholder.HorizontalWrapperViewHolder

class HorizontalWrapperAdapter(
    private val adapter: RecyclerView.Adapter<*>,
    private val space: Int? = null,
    private val customViewType: Int? = null,
    private val isDynamicHeight: Boolean = false,
) : BaseAdapter<RecyclerView.ViewHolder>() {
    var isScrollToPosition: Int? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    companion object {
        const val VIEW_TYPE = R.layout.view_horizontal_wrapper
        const val MAX_ITEM = 1
    }

    override fun getItemViewType(position: Int): Int = customViewType ?: VIEW_TYPE

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        HorizontalWrapperViewHolder.from(parent, space, isDynamicHeight, isScrollToPosition)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        when (holder) {
            is HorizontalWrapperViewHolder -> holder.bind(adapter, isScrollToPosition)
        }
    }

    override fun getItemCount(): Int = MAX_ITEM
}
