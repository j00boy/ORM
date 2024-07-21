package com.orm.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.orm.R
import com.orm.data.model.Trace
import com.orm.databinding.ActivityMainBinding
import com.orm.viewmodel.TraceViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val traceViewModel: TraceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
        moveToLoginActivity()
        getFirebaseToken()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        NavigationUI.setupWithNavController(binding.navView, navController)

//        init()
    }


    private fun init() {
        traceViewModel.insertTrace(
            Trace(
                1,
                "test",
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                1.0,
                1,
                "test",
                listOf(1, 2, 3),
                1
            )
        )
        Log.e("traceViewModel", "InsertTraces()")

        traceViewModel.traces.observe(this) {
            Log.e("traceViewModel", "traces: $it")
        }

        traceViewModel.getTrace(1)
        traceViewModel.trace.observe(this) {
            Log.e("traceViewModel", "getTrace(1): $it")
        }

        traceViewModel.trace.observe(this) {
            traceViewModel.deleteTrace(it)
        }
        Log.e("traceViewModel", "deleteTraces()")

        traceViewModel.traces.observe(this) {
            Log.e("traceViewModel", "getTraces(): $it")
        }
    }

    private fun moveToLoginActivity() {
        if (checkAccessToken()) {
            return
        }
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intent)
    }

    // TODO : Firebase Token 서버에 전송 / Login Activity 이전
    // Firebase 토큰 가져오기
    private fun getFirebaseToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("FirebaseMessaging", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            Log.d("token", task.result)
        })
    }


    // TODO : 토큰 체크 기능 구현
    private fun checkAccessToken(): Boolean {
        return true
    }
}

// reference
// https://medium.com/@wind.orca.pe/caused-by-java-lang-illegalstateexception-activity-does-not-have-a-navcontroller-f488a9b4ad5