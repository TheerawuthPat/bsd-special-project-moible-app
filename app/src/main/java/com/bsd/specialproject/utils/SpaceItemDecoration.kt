package com.bsd.specialproject.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class SpaceItemDecoration(
    private val space: Int,
    private val verticalOrientation: Boolean = true,
    private val includeEdge: Boolean = false,
    private val reverse: Boolean = false
) : ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val itemCount = parent.adapter?.itemCount.toDefaultValue()

        if (verticalOrientation) {
            if (reverse) {
                if (parent.getChildAdapterPosition(view) == itemCount - 1) {
                    outRect.set(
                        0,
                        0,
                        0,
                        0
                    )
                } else {
                    outRect.set(0, space, 0, 0)
                }
            } else {
                if (parent.getChildAdapterPosition(view) == 0) {
                    outRect.set(
                        0,
                        0,
                        0,
                        0
                    )
                } else {
                    outRect.set(0, space, 0, 0)
                }
            }
        } else {
            when {
                parent.getChildAdapterPosition(view) == 0 -> {
                    outRect.set(
                        if (includeEdge) space else 0,
                        if (includeEdge) space else 0,
                        space,
                        if (includeEdge) space else 0
                    )
                }

                parent.getChildAdapterPosition(view) >= itemCount - 1 -> {
                    outRect.set(
                        0,
                        if (includeEdge) space else 0,
                        if (includeEdge) space else 0,
                        if (includeEdge) space else 0
                    )
                }

                else -> {
                    outRect.set(
                        0,
                        if (includeEdge) space else 0,
                        space,
                        if (includeEdge) space else 0
                    )
                }
            }
        }
    }
}
