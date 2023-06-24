package com.bsd.specialproject.ui.searchresult.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bsd.specialproject.R
import com.bsd.specialproject.databinding.ItemBestOfCreditCardViewBinding
import com.bsd.specialproject.ui.addcreditcard.adapter.click.CreditCardClick
import com.bsd.specialproject.ui.searchresult.model.CreditCardSearchResultModel
import com.bsd.specialproject.utils.loadImage
import timber.log.Timber

class BestOfCreditCardViewHolder(
    val binding: ItemBestOfCreditCardViewBinding,
    val onClicked: ((CreditCardClick) -> Unit)? = null
) : RecyclerView.ViewHolder(binding.root) {

    private val viewContext by lazy {
        binding.root.context
    }

    companion object {
        fun from(
            parent: ViewGroup,
            onClicked: ((CreditCardClick) -> Unit)? = null
        ): BestOfCreditCardViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemBestOfCreditCardViewBinding.inflate(layoutInflater, parent, false)
            return BestOfCreditCardViewHolder(binding, onClicked)
        }
    }

    fun bind(item: CreditCardSearchResultModel) {
        with(binding) {
            loadImage(
                url = item.image,
                imageView = ivCreditCard,
                placeholderRes = R.drawable.placeholder_credit_card,
            )
            if (item.isCashbackHighest) {
                ivCreditCard.strokeColor = viewContext.getColorStateList(R.color.blue_dark_300)
                ivCreditCard.strokeWidth = 2f
            }
        }
    }
}
