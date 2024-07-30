package com.orm.ui.trace

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orm.R
import com.orm.data.model.Mountain
import com.orm.data.model.Trace
import com.orm.databinding.ActivityTraceBinding
import com.orm.ui.adapter.ProfileBasicAdapter
import com.orm.ui.adapter.ProfileNumberAdapter
import com.orm.ui.mountain.MountainDetailActivity
import com.orm.viewmodel.TraceViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TraceActivity : AppCompatActivity() {
    private val binding: ActivityTraceBinding by lazy {
        ActivityTraceBinding.inflate(layoutInflater)
    }
    private val traceViewModel: TraceViewModel by viewModels()
    private val rvBoard: RecyclerView by lazy { binding.recyclerView }
    private lateinit var adapter: ProfileNumberAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        traceViewModel.getTraces()
        traceViewModel.traces.observe(this@TraceActivity){
            setupAdapter(it!!)
        }

        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.edit -> {
                    startActivity(Intent(this, TraceEditActivity::class.java))
                    true
                }

                else -> false
            }
        }
    }
    private fun setupAdapter(traces: List<Trace>) {
        adapter =
            ProfileNumberAdapter(traces.map { Trace.toRecyclerViewNumberItem(it) })

        adapter.setItemClickListener(object : ProfileNumberAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val intent = Intent(
                    this@TraceActivity,
                    TraceDetailActivity::class.java
                ).apply {
                    putExtra("trace", traces[position])
                }
                startActivity(intent)
            }
        })
        rvBoard.adapter = adapter
        rvBoard.layoutManager = LinearLayoutManager(this@TraceActivity)
    }
}