package com.orm.ui

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.orm.databinding.ActivityMainBinding
import com.orm.util.PermissionManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val navHostFragment by lazy {
        supportFragmentManager.findFragmentById(binding.navHostFragmentActivityMain.id) as NavHostFragment
    }
    private lateinit var permissionManager: PermissionManager

    private val permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_NETWORK_STATE
    )

    private val notificationsPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(Manifest.permission.POST_NOTIFICATIONS)
    } else {
        emptyArray()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MainActivity", "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        NavigationUI.setupWithNavController(binding.navView, navHostFragment.navController)

        permissionManager = PermissionManager(this)
        permissionManager.initializeLauncher()

        binding.root.post {
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        val allPermissions = permissions + notificationsPermission
        if (!permissionManager.hasPermissions(allPermissions)) {
            permissionManager.checkAndRequestPermissions(allPermissions)
        }
    }
}
