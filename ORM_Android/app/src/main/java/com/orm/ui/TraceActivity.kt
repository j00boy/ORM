package com.orm.ui

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.orm.R
import com.orm.data.model.Trace
import com.orm.data.model.recycler.RecyclerViewNotificationItem
import com.orm.databinding.ActivityTraceBinding
import com.orm.ui.adapter.ProfileNotificationAdapter
import com.orm.ui.adapter.ProfileNumberAdapter

class TraceActivity : AppCompatActivity() {
    private val binding: ActivityTraceBinding by lazy {
        ActivityTraceBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val rvBoard = binding.recyclerView
        val adapter = ProfileNumberAdapter(
            listOf(
                Trace.toRecyclerViewNumberItem(
                    Trace(
                        id = 0,
                        title = "제목",
                        hikingDate = "2024.07.07",
                        hikingEndedAt = "2024.07.07",
                        hikingStartedAt = "2024.07.07",
                        maxHeight = 100.0,
                        mountainId = 0,
                        mountainName = "산 이름",
                        coordinates = null,
                        trailId = 0
                    )
                ),Trace.toRecyclerViewNumberItem(
                    Trace(
                        id = 0,
                        title = "제목",
                        hikingDate = "2024.07.07",
                        hikingEndedAt = "2024.07.07",
                        hikingStartedAt = "2024.07.07",
                        maxHeight = 100.0,
                        mountainId = 0,
                        mountainName = "산 이름",
                        coordinates = null,
                        trailId = 0
                    )
                ),Trace.toRecyclerViewNumberItem(
                    Trace(
                        id = 0,
                        title = "제목",
                        hikingDate = "2024.07.07",
                        hikingEndedAt = "2024.07.07",
                        hikingStartedAt = "2024.07.07",
                        maxHeight = 100.0,
                        mountainId = 0,
                        mountainName = "산 이름",
                        coordinates = null,
                        trailId = 0
                    )
                ),
                Trace.toRecyclerViewNumberItem(
                    Trace(
                        id = 0,
                        title = "제목",
                        hikingDate = "2024.07.07",
                        hikingEndedAt = "2024.07.07",
                        hikingStartedAt = "2024.07.07",
                        maxHeight = 100.0,
                        mountainId = 0,
                        mountainName = "산 이름",
                        coordinates = null,
                        trailId = 0
                    )
                ),
            )
        )

        adapter.setItemClickListener(object : ProfileNumberAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {}
        })

        rvBoard.adapter = adapter
        rvBoard.layoutManager =
            LinearLayoutManager(this@TraceActivity, LinearLayoutManager.VERTICAL, false)

    }
}