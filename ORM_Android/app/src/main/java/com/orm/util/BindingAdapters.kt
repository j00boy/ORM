package com.orm.util

import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.orm.R
import java.io.File

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

    @BindingAdapter("imageUri")
    @JvmStatic
    fun setImageUri(imageView: ImageView, imgPath: String?) {
        imgPath?.let {
            val file = File(it)
            if (file.exists()) {
                Glide.with(imageView.context)
                    .load(file)
                    .into(imageView)
            }
        }
    }
}
