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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LauncherActivity : AppCompatActivity() {
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var binding: ActivityLauncherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityLauncherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        CoroutineScope(Dispatchers.Main).launch {
            val token = userViewModel.getAccessToken()
            handleToken(token.toString())
        }
    }

    private fun handleToken(token: String?) {
        Log.d("LauncherActivity", "token: $token")
            navigateToActivity(MainActivity::class.java)
        return
        if (token.isNullOrEmpty()) {
            Log.d("LauncherActivity", "checkAccessToken: true, token: $token")
            userViewModel.loginAuto()
            navigateToActivity(MainActivity::class.java)
        } else {
            Log.d("LauncherActivity", "checkAccessToken: false")
            navigateToActivity(LoginActivity::class.java)
        }
    }

    private fun navigateToActivity(activityClass: Class<*>) {
        startActivity(Intent(this, activityClass).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        })
        finish()
    }
}
