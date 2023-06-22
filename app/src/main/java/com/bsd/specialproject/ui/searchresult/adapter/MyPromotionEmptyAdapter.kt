package com.bsd.specialproject.ui.searchresult.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bsd.specialproject.R
import com.bsd.specialproject.ui.searchresult.viewholder.MyPromotionEmptyViewHolder

class MyPromotionEmptyAdapter : RecyclerView.Adapter<MyPromotionEmptyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPromotionEmptyViewHolder {
        return MyPromotionEmptyViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: MyPromotionEmptyViewHolder, position: Int) {

    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_my_promotion_empty_view
    }
}
