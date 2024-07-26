package com.orm.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.orm.databinding.ActivityClubDetailBinding

class ClubDetailActivity : AppCompatActivity() {
    private val binding: ActivityClubDetailBinding by lazy {
        ActivityClubDetailBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}