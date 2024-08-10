package com.orm.util

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PermissionManager(private val activity: AppCompatActivity) {

    private lateinit var multiplePermissionsLauncher: ActivityResultLauncher<Array<String>>

    fun initializeLauncher() {
        multiplePermissionsLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            permissions.entries.forEach { (permission, isGranted) ->
                when {
                    isGranted -> {
                        Log.d("PermissionManager", "$permission granted")
                    }

                    else -> {
                        Log.d("PermissionManager", "$permission denied")
                        if (ActivityCompat.shouldShowRequestPermissionRationale(
                                activity,
                                permission
                            )
                        ) {
                            showPermissionRationale(permission)
                        } else {
                            showPermissionSettings(permission)
                        }
                    }
                }
            }
        }
    }

    private fun showPermissionRationale(permission: String) {
        MaterialAlertDialogBuilder(activity)
            .setTitle("권한 요청")
            .setMessage("해당 권한이 필요합니다: $permission")
            .setPositiveButton("확인") { _, _ ->
                multiplePermissionsLauncher.launch(arrayOf(permission))
            }
            .setNegativeButton("취소") { _, _ ->
                // 취소 버튼을 누른 경우의 코드
            }
            .show()
    }

    private fun showPermissionSettings(permission: String) {
        MaterialAlertDialogBuilder(activity)
            .setTitle("권한 필요")
            .setMessage("해당 권한이 필요합니다. 설정에서 권한을 허용해 주세요: $permission")
            .setPositiveButton("설정으로 이동") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", activity.packageName, null)
                }
                activity.startActivity(intent)
            }
            .setNegativeButton("취소") { _, _ ->
                // 취소 버튼을 누른 경우의 코드
            }
            .show()
    }

    fun checkAndRequestPermissions(permissions: Array<String>) {
        if (!hasPermissions(permissions)) {
            Log.d("PermissionManager", "모든 권한이 부여되지 않았습니다")
            multiplePermissionsLauncher.launch(permissions)
        } else {
            Log.d("PermissionManager", "모든 권한이 부여되었습니다")
        }
    }

    fun hasPermissions(permissions: Array<String>): Boolean {
        return permissions.all { permission ->
            ContextCompat.checkSelfPermission(
                activity,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
    }
}
