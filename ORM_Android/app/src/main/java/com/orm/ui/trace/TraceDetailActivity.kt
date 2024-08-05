package com.orm.ui.trace

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.orm.R
import com.orm.data.model.Trace
import com.orm.databinding.ActivityTraceDetailBinding
import com.orm.ui.fragment.map.BasicGoogleMapFragment
import com.orm.viewmodel.TraceViewModel
import com.orm.viewmodel.TrailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TraceDetailActivity : AppCompatActivity() {
    private val binding: ActivityTraceDetailBinding by lazy {
        ActivityTraceDetailBinding.inflate(layoutInflater)
    }
    private val traceViewModel: TraceViewModel by viewModels()
    private val trailViewModel: TrailViewModel by viewModels()

    private val createTraceLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val traceCreated = data?.getBooleanExtra("traceCreated", false) ?: false
                if (traceCreated) {
                    traceViewModel.getTraces()
                }
            }
        }

    private val trace: Trace? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("trace", Trace::class.java)
        } else {
            intent.getParcelableExtra<Trace>("trace")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.fcvMap.id, BasicGoogleMapFragment())
                .commit()
        }

        binding.trace = trace

        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.edit -> {
                    val intent: Intent
                    Log.d("traceTest", trace.toString())
                    if (trace?.maxHeight == 0.0) {
                        intent = Intent(this, TraceEditActivity::class.java)
                        Log.d("traceTest", "Edit")
                    } else {
                        intent = Intent(this, TraceDetailEditActivity::class.java)
                        Log.d("traceTest", "DetailEdit")
                    }
                    intent.putExtra("trace", trace)
                    createTraceLauncher.launch(intent)
                    true
                }

                else -> false
            }
        }

        binding.btnStart.setOnClickListener {
            val intent = Intent(this, TraceMeasureActivity::class.java)
            intent.putExtra("trace", trace)
            startActivity(intent)
        }

        // 등산로 선택하지 않은 경우 지도 안보임
        if (trace!!.trailId == -1 || trace!!.trailId == null) {
            binding.cvMap.visibility = View.GONE
        } else {
            trailViewModel.getTrail(trace!!.trailId!!)
            trailViewModel.trail.observe(this@TraceDetailActivity) {
                val fragment =
                    supportFragmentManager.findFragmentById(binding.fcvMap.id) as? BasicGoogleMapFragment
                fragment?.updatePoints(it.trailDetails)
            }
        }

        // 측정 기록이 없는 경우 측정 테이블 안보임
        if (trace!!.hikingStartedAt.isNullOrEmpty()) {
            binding.fcvTable.visibility = View.GONE
        } else {
            binding.btnStart.visibility = View.GONE
        }
    }
}