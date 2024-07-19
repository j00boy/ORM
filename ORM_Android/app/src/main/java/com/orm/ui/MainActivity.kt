package com.orm.ui

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.orm.R
import com.orm.data.model.Trace
import com.orm.databinding.ActivityMainBinding
import com.orm.viewmodel.TraceViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val traceViewModel: TraceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        NavigationUI.setupWithNavController(binding.navView, navController)

        init()
    }

    private fun init() {
        traceViewModel.insertTrace(
            Trace(
                1,
                "test",
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                1.0,
                1,
                "test",
                listOf(1, 2, 3),
                1
            )
        )

        traceViewModel.getTraces()
        traceViewModel.traces.observe(this) {
            Log.e("MainActivity", "init: $it")
        }
    }
}

// reference
// https://medium.com/@wind.orca.pe/caused-by-java-lang-illegalstateexception-activity-does-not-have-a-navcontroller-f488a9b4ad5