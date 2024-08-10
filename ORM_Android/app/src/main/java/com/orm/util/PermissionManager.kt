package com.orm.util

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PermissionManager(private val activity: AppCompatActivity) {

    private lateinit var permissionsLauncher: ActivityResultLauncher<Array<String>>

    fun initializeLauncher() {
        Log.d("PermissionManager", "initializeLauncher called")
        permissionsLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            handlePermissionsResult(permissions)
        }
    }

    private fun handlePermissionsResult(permissions: Map<String, Boolean>) {
        Log.d("PermissionManager", "handlePermissionsResult: $permissions")
        permissions.forEach { (permission, isGranted) ->
            if (isGranted) {
                Log.d("PermissionManager", "$permission granted")
            } else {
                Log.d("PermissionManager", "$permission denied")
                if (activity.shouldShowRequestPermissionRationale(permission)) {
                    showPermissionRationale(permission)
                } else {
                    showPermissionSettings(permission)
                }
            }
        }
    }

    private fun showPermissionRationale(permission: String) {
        MaterialAlertDialogBuilder(activity)
            .setTitle("권한 요청")
            .setMessage("해당 권한이 필요합니다: $permission")
            .setPositiveButton("확인") { _, _ ->
                permissionsLauncher.launch(arrayOf(permission))
            }
            .setNegativeButton("취소", null)
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
            .setNegativeButton("취소", null)
            .show()
    }

    fun checkAndRequestPermissions(permissions: Array<String>) {
        val permissionsToRequest = getUngrantedPermissions(permissions)
        if (permissionsToRequest.isNotEmpty()) {
            Log.d("PermissionManager", "권한 요청: ${permissionsToRequest.contentToString()}")
            permissionsLauncher.launch(permissionsToRequest)
        } else {
            Log.d("PermissionManager", "모든 권한이 부여되었습니다")
        }
    }

    private fun getUngrantedPermissions(permissions: Array<String>): Array<String> {
        return permissions.filter { permission ->
            ContextCompat.checkSelfPermission(
                activity,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()
    }
}
