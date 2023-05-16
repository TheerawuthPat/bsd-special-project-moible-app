package com.bsd.specialproject.ui.home.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bsd.specialproject.R
import com.bsd.specialproject.databinding.ItemMyCreditCardViewBinding
import com.bsd.specialproject.ui.addcreditcard.adapter.click.CreditCardClick
import com.bsd.specialproject.ui.addcreditcard.model.CreditCardModel
import com.bsd.specialproject.utils.loadImage
import com.bsd.specialproject.utils.toDefaultValue

class MyCreditCardViewHolder(
    val binding: ItemMyCreditCardViewBinding,
    val onClicked: ((CreditCardClick) -> Unit)? = null
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(
            parent: ViewGroup,
            onClicked: ((CreditCardClick) -> Unit)? = null
        ): MyCreditCardViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemMyCreditCardViewBinding.inflate(layoutInflater, parent, false)
            return MyCreditCardViewHolder(binding, onClicked)
        }
    }

    fun bind(item: CreditCardModel) {
        with(binding) {
            loadImage(
                url = item.imageUrl.toDefaultValue(),
                imageView = ivCreditCard,
                placeholderRes = R.drawable.placeholder_credit_card,
            )
//            tvCreditCardName.text = item.name.toDefaultValue()
        }
    }
}
