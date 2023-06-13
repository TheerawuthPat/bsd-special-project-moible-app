package com.bsd.specialproject.ui.common.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bsd.specialproject.databinding.ViewHorizontalWrapperBinding
import com.bsd.specialproject.utils.SpaceItemDecoration
import com.bsd.specialproject.utils.enforceSingleScrollDirection
import com.google.android.flexbox.*

class HorizontalWrapperViewHolder(
    private val binding: ViewHorizontalWrapperBinding,
    private val space: Int? = null,
    private val isDynamicHeight: Boolean = false,
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(
            parent: ViewGroup,
            space: Int? = null,
            isDynamicHeight: Boolean = false
        ): HorizontalWrapperViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ViewHorizontalWrapperBinding.inflate(layoutInflater, parent, false)
            return HorizontalWrapperViewHolder(
                binding,
                space,
                isDynamicHeight
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

    fun bind(adapter: RecyclerView.Adapter<*>) {
        val context = binding.root.context
        val flexBox = FlexboxLayoutManager(context).apply {
            flexDirection = FlexDirection.ROW
            flexWrap = FlexWrap.NOWRAP
        }
        binding.layoutHorizontalView.layoutManager = if (isDynamicHeight) {
            flexBox
        } else {
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        binding.layoutHorizontalView.enforceSingleScrollDirection()
        binding.layoutHorizontalView.adapter = adapter
    }
}
