package com.orm.ui.mountain

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.orm.data.model.Mountain
import com.orm.data.model.club.Club
import com.orm.databinding.ActivityMountainDetailBinding
import com.orm.ui.adapter.ProfileBasicAdapter
import com.orm.ui.club.ClubDetailActivity
import com.orm.viewmodel.ClubViewModel
import com.orm.viewmodel.MountainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MountainDetailActivity : AppCompatActivity() {
    private val binding: ActivityMountainDetailBinding by lazy {
        ActivityMountainDetailBinding.inflate(layoutInflater)
    }
    private val mountainViewModel: MountainViewModel by viewModels()
    private val clubViewModel: ClubViewModel by viewModels()
    private val rvBoard: RecyclerView by lazy { binding.recyclerView }
    private lateinit var adapter: ProfileBasicAdapter

    private val mountain: Mountain? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("mountain", Mountain::class.java)
        } else {
            intent.getParcelableExtra<Mountain>("mountain")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        mountainViewModel.fetchMountainById(mountain!!.id)

        binding.mountain = mountain

//        if (mountain != null) {
//            mountain!!.imageSrc!!.getNetworkImage(binding.root.context, binding.ivThumbnail)
//        }
        clubViewModel.findClubsByMountain(mountain!!.id)
        clubViewModel.clubs.observe(this@MountainDetailActivity){
            Log.d("clubTest", it.toString())
            setupAdapter(it!!)
        }
        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.mountain = mountain
    }

    private fun String.getNetworkImage(context: Context, view: ImageView) {
        Glide.with(context)
            .load(this)
            .centerCrop()
            .into(view)
    }

    private fun setupAdapter(clubs: List<Club>) {
        adapter =
            ProfileBasicAdapter(clubs.map { Club.toRecyclerViewBasicItem(it) })

        adapter.setItemClickListener(object : ProfileBasicAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val intent = Intent(
                    this@MountainDetailActivity,
                    ClubDetailActivity::class.java
                ).apply {
                    putExtra("club", clubs[position])
                }
                startActivity(intent)
            }
        })
        rvBoard.adapter = adapter
        rvBoard.layoutManager = LinearLayoutManager(this@MountainDetailActivity)
    }
}