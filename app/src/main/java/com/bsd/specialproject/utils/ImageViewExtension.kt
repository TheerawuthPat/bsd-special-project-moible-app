package com.bsd.specialproject.utils

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

fun loadImage(
    url: String,
    imageView: ImageView,
    @DrawableRes
    placeholderRes: Int
) {
    Glide.with(imageView.context)
        .asBitmap()
        .load(url)
        .placeholder(placeholderRes)
        .error(placeholderRes)
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                imageView.setImageBitmap(resource)
            }

            override fun onLoadCleared(placeholder: Drawable?) {

            }

        })
}
