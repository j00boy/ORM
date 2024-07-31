package com.orm.ui

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.orm.R
import com.orm.databinding.ActivityMainBinding
import com.orm.util.PermissionManager
import com.orm.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val navHostFragment by lazy {
        supportFragmentManager.findFragmentById(binding.navHostFragmentActivityMain.id) as NavHostFragment
    }
    private lateinit var permissionManager: PermissionManager

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.POST_NOTIFICATIONS,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        checkPermissions()
        NavigationUI.setupWithNavController(binding.navView, navHostFragment.navController)
    }

    private fun checkPermissions() {
        permissionManager = PermissionManager(this)
        permissionManager.initializeLauncher()
        permissionManager.checkAndRequestPermissions(permissions)
    }
}

// reference
// https://medium.com/@wind.orca.pe/caused-by-java-lang-illegalstateexception-activity-does-not-have-a-navcontroller-f488a9b4ad5