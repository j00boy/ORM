package com.orm.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.orm.data.model.recycler.RecyclerViewBasicItem
import com.orm.databinding.ActivityMountainSearchBinding
import com.orm.ui.adapter.ProfileBasicAdapter

class MountainSearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMountainSearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMountainSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

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
    }
}