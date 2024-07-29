package com.orm.ui.club

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.orm.R
import com.orm.databinding.ActivityClubBinding

class ClubActivity : AppCompatActivity() {
    private val binding: ActivityClubBinding by lazy {
        ActivityClubBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.edit -> {
                    startActivity(Intent(this, ClubEditActivity::class.java))
                    true
                }

                else -> false
            }
        }

        binding.tvThumbnail.setOnClickListener {
            startActivity(Intent(this, ClubSearchActivity::class.java))
        }
    }
}