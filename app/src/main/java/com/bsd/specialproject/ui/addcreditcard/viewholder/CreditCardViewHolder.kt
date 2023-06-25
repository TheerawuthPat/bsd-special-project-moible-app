package com.bsd.specialproject.ui.addcreditcard.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bsd.specialproject.R
import com.bsd.specialproject.databinding.ItemCreditCardViewBinding
import com.bsd.specialproject.ui.addcreditcard.adapter.click.CreditCardClick
import com.bsd.specialproject.ui.addcreditcard.model.CreditCardResponse
import com.bsd.specialproject.utils.loadImage
import com.bsd.specialproject.utils.toDefaultValue
import timber.log.Timber

class CreditCardViewHolder(
    private val binding: ItemCreditCardViewBinding,
    private val isEnableDelete: Boolean,
    private val onClicked: ((CreditCardClick) -> Unit)? = null
) : RecyclerView.ViewHolder(binding.root) {

    private val viewContext by lazy {
        binding.root.context
    }

    init {
    }

    companion object {
        fun from(
            parent: ViewGroup,
            isEnableDelete: Boolean,
            onClicked: ((CreditCardClick) -> Unit)? = null
        ): CreditCardViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemCreditCardViewBinding.inflate(layoutInflater, parent, false)
            return CreditCardViewHolder(binding, isEnableDelete, onClicked)
        }
    }

    fun bind(item: CreditCardResponse) {
        item.isChecked = false
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
                if(isChecked && isEnableDelete) {
                    checkbox.setButtonIconDrawableResource(R.drawable.baseline_indeterminate_check_box_20)
                    viewWhitePaper.isVisible = true
                } else {
                    checkbox.setButtonIconDrawableResource(R.drawable.baseline_check_box_outline_blank_20)
                    viewWhitePaper.isVisible = false
                }
            }
        }
    }
}
