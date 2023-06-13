package com.bsd.specialproject.ui.home.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bsd.specialproject.databinding.ItemTitleViewAllViewBinding
import com.bsd.specialproject.ui.home.model.ViewTitleModel

class TitleViewAllViewHolder(
    private val binding: ItemTitleViewAllViewBinding
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(parent: ViewGroup): TitleViewAllViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding =
                ItemTitleViewAllViewBinding.inflate(layoutInflater, parent, false)
            return TitleViewAllViewHolder(binding)
        }
    }

    private val viewContext by lazy {
        binding.root.context
    }

    fun bind(item: ViewTitleModel, onClick: () -> Unit) = with(binding) {
        with(item) {
            tvTitle.apply {
                text = title
            }
            tvViewAll.apply {
                isVisible = item.isShowViewAll
                setOnClickListener {
                    onClick.invoke()
                }
            }
        }
    }
}
