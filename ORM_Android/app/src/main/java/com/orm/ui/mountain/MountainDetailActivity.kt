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
import com.orm.R
import com.orm.data.model.Mountain
import com.orm.data.model.club.Club
import com.orm.data.model.weather.Weather
import com.orm.databinding.ActivityMountainDetailBinding
import com.orm.ui.adapter.ProfileBasicAdapter
import com.orm.ui.club.ClubDetailActivity
import com.orm.ui.fragment.WeatherFragment
import com.orm.viewmodel.ClubViewModel
import com.orm.viewmodel.MountainViewModel
import com.orm.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MountainDetailActivity : AppCompatActivity() {
    private val binding: ActivityMountainDetailBinding by lazy {
        ActivityMountainDetailBinding.inflate(layoutInflater)
    }
    private val mountainViewModel: MountainViewModel by viewModels()
    private val clubViewModel: ClubViewModel by viewModels()
    private val weatherViewModel: WeatherViewModel by viewModels()
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

        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.mountain = mountain

        mountainViewModel.fetchMountainById(mountain!!.id)

        clubViewModel.findClubsByMountain(mountain!!.id)
        clubViewModel.clubs.observe(this@MountainDetailActivity){
            Log.d("clubTest", it.toString())
            setupAdapter(it!!)
        }

        //weather
        val lat = 37.74913611
        val lon = 128.8784972
        weatherViewModel.getWeather(lat, lon)
        weatherViewModel.weather.observe(this) { weather ->
            weather?.let { updateWeather(it) }
        }

        // Add WeatherFragment dynamically
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fcv_weather, WeatherFragment())
                .commit()
        }
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

    private fun updateWeather(weather: Weather) {
        val fragment = supportFragmentManager.findFragmentById(R.id.fcv_weather) as WeatherFragment?
        fragment?.updateWeather(weather)
    }
}