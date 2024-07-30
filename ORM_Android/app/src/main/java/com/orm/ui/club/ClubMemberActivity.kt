package com.orm.ui.club

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.orm.R
import com.orm.databinding.ActivityClubMemberBinding

class ClubMemberActivity : AppCompatActivity() {
    private val binding: ActivityClubMemberBinding by lazy {
        ActivityClubMemberBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


    }
}