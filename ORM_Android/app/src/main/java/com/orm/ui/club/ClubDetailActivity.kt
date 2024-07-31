package com.orm.ui.club

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.orm.data.model.club.Club
import com.orm.databinding.ActivityClubDetailBinding
import com.orm.viewmodel.ClubViewModel
import com.orm.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClubDetailActivity : AppCompatActivity() {
    private val binding: ActivityClubDetailBinding by lazy {
        ActivityClubDetailBinding.inflate(layoutInflater)
    }

    private val userViewModel: UserViewModel by viewModels()
    private val clubViewModel: ClubViewModel by viewModels()

    private val club: Club? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("club", Club::class.java)
        } else {
            intent.getParcelableExtra<Club>("club")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Log.d("ClubDetailActivity", "club: $club")

        binding.club = club

        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnMember.setOnClickListener {
            if (club?.isMember == true) {
                startActivity(Intent(this, ClubMemberActivity::class.java))
            }
        }

        userViewModel.user.observe(this) {
            if (it != null && it.userId == club?.managerId) {
                binding.btnEdit.visibility = View.VISIBLE
            } else {
                binding.btnEdit.visibility = View.INVISIBLE
            }
        }

        binding.btnEdit.setOnClickListener {
            val intent = Intent(this, ClubEditActivity::class.java)
            intent.putExtra("club", club)
            startActivity(intent)
        }

    }
}