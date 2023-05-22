package com.bsd.specialproject.ui.home.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bsd.specialproject.databinding.ItemEmptyViewBinding

class EmptyViewHolder(
    val binding: ItemEmptyViewBinding
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(
            parent: ViewGroup
        ): EmptyViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemEmptyViewBinding.inflate(layoutInflater, parent, false)
            return EmptyViewHolder(binding)
        }
    }
}
