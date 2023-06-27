package com.bsd.specialproject.ui.searchresult.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bsd.specialproject.R
import com.bsd.specialproject.ui.searchresult.viewholder.CreditCardBenefitEmptyViewHolder

class CreditCardBenefitEmptyAdapter : RecyclerView.Adapter<CreditCardBenefitEmptyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CreditCardBenefitEmptyViewHolder {
        return CreditCardBenefitEmptyViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: CreditCardBenefitEmptyViewHolder, position: Int) {

    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_credit_card_benefit_empty_view
    }
}
