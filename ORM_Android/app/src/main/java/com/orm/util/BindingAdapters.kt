package com.orm.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.orm.R

object BindingAdapters {
    @BindingAdapter("imageSrc")
    @JvmStatic
    fun loadImage(view: ImageView, imageUrl: String?) {
        Glide.with(view.context)
            .load(imageUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .centerCrop()
            .into(view)
    }
}
