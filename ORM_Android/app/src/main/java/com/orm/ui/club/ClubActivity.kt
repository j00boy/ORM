package com.orm.ui.club

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.orm.R
import com.orm.databinding.ActivityClubBinding
import com.orm.ui.fragment.TabLayoutFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClubActivity : AppCompatActivity() {
    private val binding: ActivityClubBinding by lazy {
        ActivityClubBinding.inflate(layoutInflater)
    }

    private val createClubLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val clubCreated = data?.getBooleanExtra("clubCreated", false) ?: false
                Log.d("ClubActivity", "clubCreated: $clubCreated")
                Log.d("ClubActivity", "clubCreated: $clubCreated")
                Log.d("ClubActivity", "clubCreated: $clubCreated")
                Log.d("ClubActivity", "clubCreated: $clubCreated")
                Log.d("ClubActivity", "clubCreated: $clubCreated")
                Log.d("ClubActivity", "clubCreated: $clubCreated")
                if (clubCreated) {
                    refreshTabLayoutFragment()
                }
            }
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
                    createClubLauncher.launch(Intent(this, ClubEditActivity::class.java))
                    true
                }

                else -> false
            }
        }

        binding.tvThumbnail.setOnClickListener {
            startActivity(Intent(this, ClubSearchActivity::class.java))
        }
    }

    private fun refreshTabLayoutFragment() {
        val newFragment = TabLayoutFragment()
        supportFragmentManager.beginTransaction()
            .replace(binding.info.id, newFragment)
            .commitNow()
    }
}