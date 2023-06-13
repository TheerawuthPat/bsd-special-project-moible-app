package com.bsd.specialproject.ui.searchresult.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bsd.specialproject.R
import com.bsd.specialproject.databinding.ItemMyCreditCardViewBinding
import com.bsd.specialproject.databinding.ItemMyPromotionViewBinding
import com.bsd.specialproject.ui.addcreditcard.adapter.click.CreditCardClick
import com.bsd.specialproject.ui.addcreditcard.model.CreditCardModel
import com.bsd.specialproject.ui.searchresult.model.MyPromotionModel
import com.bsd.specialproject.utils.loadImage
import com.bsd.specialproject.utils.toDefaultValue

class MyPromotionViewHolder(
    val binding: ItemMyPromotionViewBinding,
    val onClicked: ((CreditCardClick) -> Unit)? = null
) : RecyclerView.ViewHolder(binding.root) {

    private val viewContext by lazy {
        binding.root.context
    }

    companion object {
        fun from(
            parent: ViewGroup,
            onClicked: ((CreditCardClick) -> Unit)? = null
        ): MyPromotionViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemMyPromotionViewBinding.inflate(layoutInflater, parent, false)
            return MyPromotionViewHolder(binding, onClicked)
        }
    }

    fun bind(item: MyPromotionModel) {
        with(binding) {
            tvPromotionName.text = "BBGON กินอิ่ม"
            tvMoreAccumulateSpending.text = viewContext.getString(R.string.more_accumulate_spending, "500")
            tvCashbackEarned.text = viewContext.getString(R.string.cashback_earned, "500")
            tvPromotionExpiredDate.text = viewContext.getString(R.string.promotion_expired_date, "30")
            tvSavedPromotionDate.text = viewContext.getString(R.string.saved_promotion_date, "12/11/2022")
        }
    }
}
