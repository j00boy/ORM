package com.orm.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.orm.R
import com.orm.databinding.ActivityClubBinding

class ClubActivity : AppCompatActivity() {
    private val binding : ActivityClubBinding by lazy {
        ActivityClubBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.tvThumbnail.setOnClickListener {
            startActivity(Intent(this, ClubSearchActivity::class.java))
        }
    }
}