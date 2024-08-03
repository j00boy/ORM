package com.orm.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.orm.util.NetworkUtils
import com.orm.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LauncherActivity : AppCompatActivity() {
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
        navigateToActivity(MainActivity::class.java)
        userViewModel.isLoading.observe(this) { isLoading ->
            if (!isLoading) {
                val token = userViewModel.token.value
                handleToken(token)
            }
        }
    }

    private fun handleToken(token: String?) {
        val isNetworkAvailable = NetworkUtils.isNetworkAvailable(this)

        if (token.isNullOrEmpty()) {
            Log.e("LauncherActivity", "checkAccessToken: false")
            navigateToActivity(LoginActivity::class.java)
        } else {
            Log.e("LauncherActivity", "checkAccessToken: true, token: $token")
            if (isNetworkAvailable) {
                userViewModel.loginAuto()
            }
            navigateToActivity(MainActivity::class.java)
        }
    }

    private fun navigateToActivity(activityClass: Class<*>) {
        startActivity(Intent(this, activityClass).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
        finish()
    }
}
