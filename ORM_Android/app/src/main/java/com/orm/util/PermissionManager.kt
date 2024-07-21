package com.orm.util

import android.app.AlertDialog
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

class PermissionManager(private val activity: AppCompatActivity) {

    private lateinit var multiplePermissionsLauncher: ActivityResultLauncher<Array<String>>

    fun initializeLauncher() {
        multiplePermissionsLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            permissions.entries.forEach { (permission, isGranted) ->
                when {
                    isGranted -> {
                        // 권한이 승인된 경우 처리할 작업
                        Log.d("Permissions", "$permission granted")
                    }
                    else -> {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                            // 권한 요청에 대한 이유를 사용자에게 설명하는 Dialog를 표시
                            AlertDialog.Builder(activity)
                                .setTitle("권한 요청")
                                .setMessage("해당 권한이 필요합니다: $permission")
                                .setPositiveButton("확인") { _, _ ->
                                    multiplePermissionsLauncher.launch(arrayOf(permission))
                                }
                                .setNegativeButton("취소") { _, _ ->
                                    // Dialog에서 취소 버튼을 누른 경우에 실행할 코드
                                }
                                .show()
                        } else {
                            // 사용자가 권한 요청 다이얼로그에서 "다시 묻지 않음" 옵션을 선택한 경우에 실행할 코드
                            AlertDialog.Builder(activity)
                                .setTitle("권한 필요")
                                .setMessage("해당 권한이 필요합니다. 설정에서 권한을 허용해 주세요: $permission")
                                .setPositiveButton("설정으로 이동") { _, _ ->
                                    // 설정 화면으로 이동하도록 사용자에게 안내
                                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                        data = Uri.fromParts("package", activity.packageName, null)
                                    }
                                    activity.startActivity(intent)
                                }
                                .setNegativeButton("취소") { _, _ ->
                                    // Dialog에서 취소 버튼을 누른 경우 실행할 코드
                                    activity.finish()
                                }
                                .show()
                        }
                    }
                }
            }
        }
    }

    fun checkAndRequestPermissions(permissions: Array<String>) {
        if (!hasPermissions(permissions)) {
            multiplePermissionsLauncher.launch(permissions)
        }
    }

    private fun hasPermissions(permissions: Array<String>): Boolean {
        return permissions.all { permission ->
            ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
        }
    }
}
