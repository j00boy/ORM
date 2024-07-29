package com.orm.ui.club

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.orm.databinding.ActivityClubEditBinding

class ClubEditActivity : AppCompatActivity() {
    private val binding : ActivityClubEditBinding by lazy {
        ActivityClubEditBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}