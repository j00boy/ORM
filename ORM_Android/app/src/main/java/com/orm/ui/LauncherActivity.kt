package com.orm.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.orm.databinding.ActivityLauncherBinding
import com.orm.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LauncherActivity : AppCompatActivity() {
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityLauncherBinding.inflate(layoutInflater).root)

        if (!checkAccessToken()) {
            userViewModel.loginAuto()
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        finish()
    }

    private fun checkAccessToken(): Boolean {
        var token = ""
        userViewModel.token.observe(this) {
            token = it.toString()
        }
        return token.isNotEmpty()
    }
}