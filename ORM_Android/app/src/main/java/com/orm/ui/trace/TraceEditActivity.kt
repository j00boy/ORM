package com.orm.ui.trace

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.orm.databinding.ActivityTraceEditBinding

class TraceEditActivity : AppCompatActivity() {
    private val binding: ActivityTraceEditBinding by lazy {
        ActivityTraceEditBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}