package com.bsd.specialproject.ui.common.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bsd.specialproject.databinding.ViewHorizontalWrapperBinding
import com.bsd.specialproject.utils.*
import com.google.android.flexbox.*

class HorizontalWrapperViewHolder(
    private val binding: ViewHorizontalWrapperBinding,
    private val space: Int? = null,
    private val isDynamicHeight: Boolean = false,
    private val isScrollToPosition: Int? = null,
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(
            parent: ViewGroup,
            space: Int? = null,
            isDynamicHeight: Boolean = false,
            isScrollToPosition: Int?
        ): HorizontalWrapperViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ViewHorizontalWrapperBinding.inflate(layoutInflater, parent, false)
            return HorizontalWrapperViewHolder(
                binding,
                space,
                isDynamicHeight,
                isScrollToPosition
            )
        }
    }

    init {
        space?.let {
            binding.layoutHorizontalView.addItemDecoration(
                SpaceItemDecoration(
                    it,
                    verticalOrientation = false,
                    includeEdge = true
                )
            )
        }
    }

    fun bind(adapter: RecyclerView.Adapter<*>, isScrollToPosition: Int?) {
        val context = binding.root.context
        val flexBox = FlexboxLayoutManager(context).apply {
            flexDirection = FlexDirection.ROW
            flexWrap = FlexWrap.NOWRAP
        }
        binding.layoutHorizontalView.apply {
            layoutManager = if (isDynamicHeight) {
                flexBox
            } else {
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
            this.smoothScrollToPosition(isScrollToPosition.toDefaultValue())
            this.enforceSingleScrollDirection()
            this.adapter = adapter
        }

    }
}
