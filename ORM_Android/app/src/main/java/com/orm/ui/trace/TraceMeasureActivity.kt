package com.orm.ui.trace

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.orm.data.model.Trace
import com.orm.databinding.ActivityTraceMeasureBinding
import com.orm.ui.fragment.map.TraceGoogleMapFragment
import com.orm.viewmodel.TrailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TraceMeasureActivity : AppCompatActivity() {

    private val binding: ActivityTraceMeasureBinding by lazy {
        ActivityTraceMeasureBinding.inflate(layoutInflater)
    }

    private val trace: Trace? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("trace", Trace::class.java)
        } else {
            intent.getParcelableExtra<Trace>("trace")
        }
    }

    private val trailViewModel: TrailViewModel by viewModels()

    private var savedInstanceState: Bundle? = null

    private val requestLocationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            setupFragment(savedInstanceState)
        } else {
            showPermissionDeniedDialog()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        this.savedInstanceState = savedInstanceState

        if (isLocationPermissionGranted()) {
            setupFragment(savedInstanceState)
        } else {
            requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun setupFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            val fragment = trace?.let { trace ->
                if (trace.trailId != null) {
                    trailViewModel.getTrail(trace.trailId)
                    var trailFragment: TraceGoogleMapFragment? = null
                    trailViewModel.trail.observe(this) { trail ->
                        trailFragment = TraceGoogleMapFragment.newInstance(
                            points = trail.trailDetails,
                            traceId = trace.localId
                        )
                        supportFragmentManager.beginTransaction()
                            .replace(binding.fcvMap.id, trailFragment!!)
                            .commit()
                        trailViewModel.trail.removeObservers(this)
                    }
                    return
                } else {
                    TraceGoogleMapFragment.newInstance(emptyList(), trace.localId)
                }
            } ?: TraceGoogleMapFragment.newInstance(emptyList())

            fragment.let {
                supportFragmentManager.beginTransaction()
                    .replace(binding.fcvMap.id, it)
                    .commit()
            }
        }
    }

    private fun showPermissionDeniedDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("위치 권한 필요")
            .setMessage("이 기능을 사용하려면 위치 권한이 필요합니다. 설정에서 권한을 허용해 주세요.")
            .setPositiveButton("설정으로 이동") { dialog, _ ->
                dialog.dismiss()
                val intent =
                    Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = android.net.Uri.fromParts("package", packageName, null)
                    }
                startActivity(intent)
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .setOnDismissListener {
                onBackPressedDispatcher.onBackPressed()
            }
            .show()

    }

}
