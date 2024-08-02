package com.orm.ui.trace

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.orm.R
import com.orm.data.model.Trace
import com.orm.databinding.ActivityTraceBinding
import com.orm.ui.adapter.ProfileNumberAdapter
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        traceViewModel.getTraces()
        traceViewModel.traces.observe(this@TraceActivity) {
            Log.d("traceTest1", it.toString())
            setupAdapter(it!!)
        }

        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.edit -> {
                    createTraceLauncher.launch(Intent(this, TraceEditActivity::class.java))
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

            override fun onLongClick(v: View, position: Int) {
                MaterialAlertDialogBuilder(this@TraceActivity)
                    .setTitle("발자국 삭제")
                    .setMessage("발자국을 삭제하시겠습니까?")
                    .setPositiveButton("확인") { _, _ ->
                        traceViewModel.deleteTrace(traces[position])
                    }
                    .setNegativeButton("취소") { _, _ ->
                        // Dialog에서 취소 버튼을 누른 경우에 실행할 코드
                    }
                    .show()
            }
        })
        rvBoard.adapter = adapter
        rvBoard.layoutManager = LinearLayoutManager(this@TraceActivity)
    }
}