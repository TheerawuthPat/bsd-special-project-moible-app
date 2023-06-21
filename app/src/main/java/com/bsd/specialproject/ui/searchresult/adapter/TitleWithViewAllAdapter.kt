package com.bsd.specialproject.ui.searchresult.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bsd.specialproject.ui.home.model.ViewTitleModel
import com.bsd.specialproject.ui.home.viewholder.TitleViewAllViewHolder

class TitleWithViewAllAdapter(
    private val viewTitleModel: ViewTitleModel,
    private val onClick: () -> Unit
) : RecyclerView.Adapter<TitleViewAllViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitleViewAllViewHolder {
        return TitleViewAllViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: TitleViewAllViewHolder, position: Int) {
        holder.bind(viewTitleModel, onClick)
    }
}
