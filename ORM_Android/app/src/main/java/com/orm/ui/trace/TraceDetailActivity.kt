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

        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.trace = trace

        if (trace!!.trailId == -1) {
            binding.cvMap.visibility = View.GONE
        } else {
            trailViewModel.getTrail(trace!!.trailId!!)
            trailViewModel.trail.observe(this@TraceDetailActivity) {
                val fragment =
                    supportFragmentManager.findFragmentById(binding.fcvMap.id) as? BasicGoogleMapFragment
                fragment?.updatePoints(it.trailDetails)
            }
        }

        if (trace!!.hikingStartedAt.isNullOrEmpty()) {
            binding.fcvTable.visibility = View.GONE
        }

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.edit -> {
                    val intent = Intent(this, TraceEditActivity::class.java)
                    intent.putExtra("trace", trace)
                    createTraceLauncher.launch(intent)
                    true
                }

                else -> false
            }
        }
    }
}