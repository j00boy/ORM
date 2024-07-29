package com.orm.ui.mountain

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.orm.data.model.Mountain
import com.orm.databinding.ActivityMountainDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MountainDetailActivity : AppCompatActivity() {
    private val binding: ActivityMountainDetailBinding by lazy {
        ActivityMountainDetailBinding.inflate(layoutInflater)
    }
    private val mountain: Mountain? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("mountain", Mountain::class.java)
        } else {
            intent.getParcelableExtra<Mountain>("mountain")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//        if (mountain != null) {
//            mountain!!.imageSrc!!.getNetworkImage(binding.root.context, binding.ivThumbnail)
//        }

        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.mountain = mountain
    }

    private fun String.getNetworkImage(context: Context, view: ImageView) {
        Glide.with(context)
            .load(this)
            .centerCrop()
            .into(view)
    }
}