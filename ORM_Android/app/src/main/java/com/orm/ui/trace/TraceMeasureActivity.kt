package com.orm.ui.trace

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.orm.R
import com.orm.databinding.ActivityTraceMeasureBinding
import com.orm.ui.fragment.map.BasicGoogleMapFragment
import com.orm.ui.fragment.map.TraceGoogleMapFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TraceMeasureActivity : AppCompatActivity() {
    private val binding: ActivityTraceMeasureBinding by lazy {
        ActivityTraceMeasureBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.fcvMap.id, TraceGoogleMapFragment())
                .commit()
        }
    }
}