package com.bsd.specialproject.ui.searchresult.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bsd.specialproject.databinding.ItemCreditCardBenefitEmptyViewBinding

class CreditCardBenefitEmptyViewHolder(
    val binding: ItemCreditCardBenefitEmptyViewBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val viewContext by lazy {
        binding.root.context
    }

    companion object {
        fun from(
            parent: ViewGroup,
        ): CreditCardBenefitEmptyViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding =
                ItemCreditCardBenefitEmptyViewBinding.inflate(layoutInflater, parent, false)
            return CreditCardBenefitEmptyViewHolder(binding)
        }
    }
}
