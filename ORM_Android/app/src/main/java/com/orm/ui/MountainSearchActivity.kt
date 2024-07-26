package com.orm.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orm.data.model.Mountain
import com.orm.data.model.recycler.RecyclerViewBasicItem
import com.orm.databinding.ActivityMountainSearchBinding
import com.orm.ui.adapter.ProfileBasicAdapter
import com.orm.viewmodel.MountainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MountainSearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMountainSearchBinding
    private lateinit var adapter: ProfileBasicAdapter
    private lateinit var rvBoard: RecyclerView
    private val mountainViewModel: MountainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMountainSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.svMountain.isSubmitButtonEnabled = true
        binding.svMountain.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(name: String?): Boolean {
                Log.e("search Text", name.toString())

                mountainViewModel.fetchMountainByName(name.toString())
                mountainViewModel.mountains.observe(this@MountainSearchActivity) { it ->
                    Log.e("mountain", it.toString())


                    val profiles = ProfileBasicAdapter(
                        it.map { Mountain.toRecyclerViewBasicItem(it) }
                    )
                    Log.e("profile", profiles.toString())
                    rvBoard.adapter = profiles
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

        })

        rvBoard = binding.recyclerView
        adapter = ProfileBasicAdapter(
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