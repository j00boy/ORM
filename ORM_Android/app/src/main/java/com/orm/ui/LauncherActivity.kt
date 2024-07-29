package com.orm.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.orm.databinding.ActivityLauncherBinding
import com.orm.util.NetworkUtils
import com.orm.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LauncherActivity : AppCompatActivity() {
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().setKeepOnScreenCondition(
            condition = {
                false
            }
        )
            handleToken("")
//        userViewModel.token.observe(this) { token ->
//            handleToken(token)
//        }
    }

    private fun handleToken(token: String?) {
//        val isNetworkAvailable = NetworkUtils.isNetworkAvailable(this)

//        if (token.isNullOrEmpty()) {
//            Log.e("LauncherActivity", "checkAccessToken: false")
//            navigateToActivity(LoginActivity::class.java)
//        } else {
//            Log.e("LauncherActivity", "checkAccessToken: true, token: $token")
//            if (isNetworkAvailable) {
            // TODO : 동기 처리
//            userViewModel.loginAuto()
//            }
            navigateToActivity(MainActivity::class.java)
//        }
    }

    private fun navigateToActivity(activityClass: Class<*>) {
        startActivity(Intent(this, activityClass).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        })
        finish()
    }
}
