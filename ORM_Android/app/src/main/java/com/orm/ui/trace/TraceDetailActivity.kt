package com.orm.ui.trace

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.orm.data.model.Trace
import com.orm.databinding.ActivityTraceDetailBinding
import com.orm.viewmodel.TraceViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TraceDetailActivity : AppCompatActivity() {
    private val binding: ActivityTraceDetailBinding by lazy {
        ActivityTraceDetailBinding.inflate(layoutInflater)
    }
    private val traceViewModel: TraceViewModel by viewModels()

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

        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.trace = trace

    }
}