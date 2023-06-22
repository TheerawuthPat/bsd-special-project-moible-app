package com.bsd.specialproject.ui.searchresult.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bsd.specialproject.databinding.ItemMyPromotionEmptyViewBinding

class MyPromotionEmptyViewHolder(
    val binding: ItemMyPromotionEmptyViewBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val viewContext by lazy {
        binding.root.context
    }

    companion object {
        fun from(
            parent: ViewGroup,
        ): MyPromotionEmptyViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemMyPromotionEmptyViewBinding.inflate(layoutInflater, parent, false)
            return MyPromotionEmptyViewHolder(binding)
        }
    }
}
