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
import com.orm.ui.fragment.table.TextTableFragment
import com.orm.viewmodel.RecordViewModel
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
    private val recordViewModel: RecordViewModel by viewModels()

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

        Log.e("TraceDetailActivity", trace!!.recordId.toString())
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.fcvMap.id, BasicGoogleMapFragment())
                .commit()

            if (trace!!.recordId != null) {
                supportFragmentManager.beginTransaction()
                    .replace(binding.fcvMapTrack.id, BasicGoogleMapFragment())
                    .commit()

                supportFragmentManager.beginTransaction()
                    .replace(binding.fcvTable.id, TextTableFragment.newInstance(trace!!))
                    .commit()

            } else {
                binding.cvMapTrack.visibility = View.GONE
            }
        }

        binding.trace = trace

        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.edit -> {
                    val intent: Intent
                    if (trace?.recordId == null) {
                        intent = Intent(this, TraceEditActivity::class.java)
                    } else {
                        intent = Intent(this, TraceDetailEditActivity::class.java)
                    }
                    intent.putExtra("trace", trace)
                    createTraceLauncher.launch(intent)
                    true
                }

                else -> false
            }
        }

        // 측정 기록이 없는 경우 측정 테이블 안보임
        // 측정 완료한 경우 측정 버튼 안보임
        if (trace!!.recordId == null) {
            binding.fcvTable.visibility = View.GONE
        } else {
            binding.btnStart.visibility = View.GONE
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

            if (trace!!.recordId != null) {
                recordViewModel.getRecord(trace!!.recordId!!)
                recordViewModel.record.observe(this@TraceDetailActivity) {
                    val fragment =
                        supportFragmentManager.findFragmentById(binding.fcvMapTrack.id) as? BasicGoogleMapFragment
                    fragment?.updatePoints(it.coordinate ?: emptyList())
                }
            }
        }
    }
}