package com.bsd.specialproject.ui.searchresult.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bsd.specialproject.R
import com.bsd.specialproject.databinding.ItemTitleForYouHeaderViewBinding
import com.bsd.specialproject.ui.home.model.ViewTitleModel
import com.bsd.specialproject.ui.searchresult.adapter.click.PromotionClick

class TitleForYouHeaderViewHolder(
    private val binding: ItemTitleForYouHeaderViewBinding,
    private val onClicked: ((PromotionClick) -> Unit)? = null
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(
            parent: ViewGroup,
            onClicked: (PromotionClick) -> Unit
        ): TitleForYouHeaderViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemTitleForYouHeaderViewBinding.inflate(layoutInflater, parent, false)
            return TitleForYouHeaderViewHolder(binding, onClicked)
        }
    }

    private val viewContext by lazy {
        binding.root.context
    }

    fun bind(item: ViewTitleModel, isGrantedLocation: Boolean) = with(binding) {
        with(item) {
            tvTitle.apply {
                text = title
            }
            tvViewAll.apply {
                isVisible = item.isShowViewAll
            }
            btnFilterByDistance.apply {
                isVisible = isGrantedLocation
                if (isGrantedLocation) {
                    setBackgroundColor(viewContext.getColor(R.color.blue_light_300))
                } else {
                    alpha = 0.3f
                }
                setOnClickListener {
                    binding.btnFilterByCashback.apply {
                        alpha = 0.3f
                    }
                    setBackgroundColor(viewContext.getColor(R.color.blue_light_300))
                    onClicked?.invoke(PromotionClick.FilterByDistanceClick)
                    alpha = 1f
                }
            }
            btnFilterByCashback.apply {
                if (!isGrantedLocation) {
                    setBackgroundColor(viewContext.getColor(R.color.blue_light_300))
                } else {
                    alpha = 0.3f
                }
                setOnClickListener {
                    binding.btnFilterByDistance.apply {
                        alpha = 0.3f
                    }
                    setBackgroundColor(viewContext.getColor(R.color.blue_light_300))
                    onClicked?.invoke(PromotionClick.FilterByCashbackClick)
                    alpha = 1f
                }
            }
        }
    }
}
