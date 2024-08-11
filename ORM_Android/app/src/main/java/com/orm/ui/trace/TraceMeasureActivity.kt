package com.orm.ui.trace

import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

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
}
