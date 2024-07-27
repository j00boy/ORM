package com.orm.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
                    Log.e("ClubActivity", "edit clicked")
                    // TODO : implements club edit activity
                    Toast.makeText(this@ClubActivity, "implements club edit activity", Toast.LENGTH_LONG).show()
                    // startActivity(Intent(this, ClubEditActivity::class.java))
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