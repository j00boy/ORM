package com.orm.ui

import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.orm.data.model.Club
import com.orm.databinding.ActivityClubDetailBinding
import com.orm.viewmodel.ClubViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClubDetailActivity : AppCompatActivity() {
    private val binding: ActivityClubDetailBinding by lazy {
        ActivityClubDetailBinding.inflate(layoutInflater)
    }
//    private val club: Club? by lazy {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            intent.getParcelableExtra("club", Club::class.java)
//        } else {
//            intent.getParcelableExtra<Club>("club")
//        }
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

//        binding.club = club
    }
}