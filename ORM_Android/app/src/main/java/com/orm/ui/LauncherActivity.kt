package com.orm.ui

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.orm.databinding.ActivityLauncherBinding
import com.orm.util.NetworkUtils
import com.orm.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LauncherActivity : AppCompatActivity() {
    private val userViewModel: UserViewModel by viewModels()
    private val binding: ActivityLauncherBinding by lazy {
        ActivityLauncherBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        CoroutineScope(Dispatchers.Main).launch {
            userViewModel.getAccessToken()
            handleToken(userViewModel.token.value.toString())
        }
    }

    private fun handleToken(token: String?) {
        navigateToActivity(MainActivity::class.java)
        return
//        if (token.isNullOrEmpty() && NetworkUtils.isNetworkAvailable(this)) {
//            Log.e("LauncherActivity", "checkAccessToken: false")
//            navigateToActivity(LoginActivity::class.java)
//        } else {
//            Log.e("LauncherActivity", "checkAccessToken: true, token: $token")
//            if (NetworkUtils.isNetworkAvailable(this)) userViewModel.loginAuto()
//            navigateToActivity(MainActivity::class.java)
//        }
    }

    private fun navigateToActivity(activityClass: Class<*>) {
        startActivity(Intent(this, activityClass).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        })
        finish()
    }
}
