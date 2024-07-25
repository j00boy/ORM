package com.orm.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.orm.R
import com.orm.data.model.recycler.RecyclerViewBasicItem
import com.orm.databinding.ActivityMountainSearchBinding
import com.orm.ui.adapter.ProfileBasicAdapter

class MountainSearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMountainSearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mountain_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_mountain_search)


        val rvBoard = binding.recyclerView
        val adapter = ProfileBasicAdapter(
            listOf(
                RecyclerViewBasicItem(
                    imageSrc = "https://img.tenping.kr/Content/Upload/Images/2023021311020002_Dis_20230223085848.jpg?RS=180x120",
                    title = "안녕하세요.",
                    subTitle = "반갑습니다.",
                ),
            )
        )

        adapter.setItemClickListener(object : ProfileBasicAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                Toast.makeText(
                    this@MountainSearchActivity,
                    position.toString() + "번 클릭",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        rvBoard.adapter = adapter
        rvBoard.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        setSupportActionBar(binding.toolbar)
    }
}