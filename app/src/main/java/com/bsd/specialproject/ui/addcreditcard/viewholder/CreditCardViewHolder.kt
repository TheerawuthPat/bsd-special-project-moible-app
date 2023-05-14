package com.bsd.specialproject.ui.addcreditcard.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bsd.specialproject.R
import com.bsd.specialproject.databinding.ItemCreditCardViewBinding
import com.bsd.specialproject.ui.addcreditcard.adapter.click.CreditCardClick
import com.bsd.specialproject.ui.addcreditcard.model.CreditCardModel
import com.bsd.specialproject.utils.loadImage
import com.bsd.specialproject.utils.toDefaultValue

class CreditCardViewHolder(
    val binding: ItemCreditCardViewBinding,
    val onClicked: ((CreditCardClick) -> Unit)? = null
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(
            parent: ViewGroup,
            onClicked: ((CreditCardClick) -> Unit)? = null
        ): CreditCardViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemCreditCardViewBinding.inflate(layoutInflater, parent, false)
            return CreditCardViewHolder(binding, onClicked)
        }
    }

    fun bind(item: CreditCardModel) {
        with(binding) {
            loadImage(
                url = item.imageUrl.toDefaultValue(),
                imageView = ivCreditCard,
                placeholderRes = R.drawable.placeholder_credit_card,
            )
            tvCreditCardName.text = item.name.toDefaultValue()
            checkbox.setOnCheckedChangeListener { _, isChecked ->
                item.isChecked = isChecked
                onClicked?.invoke(CreditCardClick.SelectedClick(item))
            }
        }
    }
}
