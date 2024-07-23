package com.orm.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.orm.databinding.ActivityLauncherBinding
import com.orm.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LauncherActivity : AppCompatActivity() {
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        setContentView(ActivityLauncherBinding.inflate(layoutInflater).root)

        userViewModel.getAccessToken()
        userViewModel.token.observe(this) { token ->
            Log.d("LauncherActivity", "token: $token")
            if (!token.isNullOrEmpty()) {
                Log.d("LauncherActivity", "checkAccessToken: true, token: $token")
                userViewModel.loginAuto()
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                Log.d("LauncherActivity", "checkAccessToken: false")
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish()
        }
    }
}