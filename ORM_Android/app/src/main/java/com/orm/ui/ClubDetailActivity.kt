package com.orm.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.orm.databinding.ActivityClubDetailBinding

class ClubDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClubDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityClubDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}