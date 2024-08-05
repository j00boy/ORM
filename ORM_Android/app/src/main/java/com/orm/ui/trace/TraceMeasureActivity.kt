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
            trace?.trailId?.let { trailId ->
                trailViewModel.getTrail(trailId)

                trailViewModel.trail.observe(this) { trail ->
                    val fragment = TraceGoogleMapFragment.newInstance(
                        points = trail.trailDetails,
                        traceId = trace?.id
                    )

                    supportFragmentManager.beginTransaction()
                        .replace(binding.fcvMap.id, fragment)
                        .commit()

                    trailViewModel.trail.removeObservers(this)
                }
            } ?: run {
                val fragment = TraceGoogleMapFragment.newInstance(emptyList())
                supportFragmentManager.beginTransaction()
                    .replace(binding.fcvMap.id, fragment)
                    .commit()
            }
        }
    }
}
