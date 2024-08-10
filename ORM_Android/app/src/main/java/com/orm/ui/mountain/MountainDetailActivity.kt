package com.orm.ui.mountain

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orm.util.HikingTimePredictor
import com.orm.R
import com.orm.data.model.Mountain
import com.orm.data.model.Point
import com.orm.data.model.Trail
import com.orm.data.model.club.Club
import com.orm.data.model.weather.Weather
import com.orm.databinding.ActivityMountainDetailBinding
import com.orm.ui.adapter.ProfileBasicAdapter
import com.orm.ui.club.ClubDetailActivity
import com.orm.ui.fragment.WeatherFragment
import com.orm.ui.fragment.map.BasicGoogleMapFragment
import com.orm.ui.trace.TraceEditActivity
import com.orm.viewmodel.ClubViewModel
import com.orm.viewmodel.MountainViewModel
import com.orm.viewmodel.UserViewModel
import com.orm.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MountainDetailActivity : AppCompatActivity() {
    private val binding: ActivityMountainDetailBinding by lazy {
        ActivityMountainDetailBinding.inflate(layoutInflater)
    }

    private var mountain: Mountain? = null

    private val mountainViewModel: MountainViewModel by viewModels()
    private val clubViewModel: ClubViewModel by viewModels()
    private val weatherViewModel: WeatherViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private val rvBoard: RecyclerView by lazy { binding.recyclerView }
    private lateinit var adapter: ProfileBasicAdapter
    private var predictTimeList: List<Float> = emptyList()

    @Inject
    lateinit var hikingTimePredictor: HikingTimePredictor

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        mountain = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("mountain", Mountain::class.java)
        } else {
            intent.getParcelableExtra<Mountain>("mountain")
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fcv_weather, WeatherFragment())
                .commit()

            supportFragmentManager.beginTransaction()
                .replace(binding.fcvMap.id, BasicGoogleMapFragment())
                .commit()
        }

        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.mountain = mountain
        mountainViewModel.fetchMountainById(mountain!!.id)
        mountainViewModel.mountain.observe(this@MountainDetailActivity) {
            if (it != null && !it.trails.isNullOrEmpty()) {
                lifecycleScope.launch {
                    try {
                        predictTimeList = it.trails.map { trail ->
                            hikingTimePredictor.predictTrail(trail)
                        }
                        setupTrailSpinner(it.trails)
                        Log.d("AITEST", predictTimeList.toString())
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.d("AITEST", "error", e)
                    }
                }
                userViewModel.getUserInfo()
                userViewModel.user.observe(this@MountainDetailActivity) {
                    binding.trailHint = userViewModel.user.value?.nickname + "님의 예상 등반 시간입니다."
                }
            } else {
                binding.fcvMap.visibility = View.GONE
                binding.spinnerTrails.visibility = View.GONE
                binding.trailHint = "등산로 정보가 존재하지 않습니다."
            }
        }

        weatherViewModel.getWeather(mountain!!.addressLatitude, mountain!!.addressLongitude)
        weatherViewModel.weather.observe(this) { weather ->
            weather?.let { updateWeather(weather) }
        }

        clubViewModel.findClubsByMountain(mountain!!.id)
        clubViewModel.clubs.observe(this@MountainDetailActivity) {
            if (it.isNullOrEmpty()) {
                binding.tvClub.visibility = View.GONE
                binding.recyclerView.visibility = View.GONE
                return@observe
            }
            setupAdapter(it)
        }

        binding.btnCreateTrail.setOnClickListener {
            val intent = Intent(this, TraceEditActivity::class.java)
            intent.putExtra("trailIndex", binding.spinnerTrails.selectedItemPosition)
            intent.putExtra("mountain", mountain)
            startActivity(intent)
        }
    }

    private fun setupTrailSpinner(trails: List<Trail>) {
        val spinner = findViewById<Spinner>(binding.spinnerTrails.id)
        val trailNames = trails.map { it.distance.toString() + "km" }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, trailNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long,
            ) {
                val selectedTrail = trails[position]
                val totalSeconds = (predictTimeList[position] * 60).toInt()
                val hours = totalSeconds / 3600
                val minutes = (totalSeconds % 3600) / 60
                val seconds = totalSeconds % 60
                binding.predictTime = "${hours}시간 ${minutes}분 ${seconds}초"
                updateMapFragment(selectedTrail.trailDetails)
                weatherViewModel.getWeather(
                    selectedTrail.startLatitude,
                    selectedTrail.startLongitude
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupAdapter(clubs: List<Club>) {
        adapter = ProfileBasicAdapter(clubs.map { Club.toRecyclerViewBasicItem(it) })

        adapter.setItemClickListener(object : ProfileBasicAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val intent =
                    Intent(this@MountainDetailActivity, ClubDetailActivity::class.java).apply {
                        putExtra("club", clubs[position])
                    }
                startActivity(intent)
            }
        })
        rvBoard.adapter = adapter
        rvBoard.layoutManager = LinearLayoutManager(this@MountainDetailActivity)
    }

    private fun updateMapFragment(points: List<Point>) {
        val fragment =
            supportFragmentManager.findFragmentById(binding.fcvMap.id) as? BasicGoogleMapFragment
        fragment?.updatePoints(points)
    }

    private fun updateWeather(weather: Weather) {
        val fragment =
            supportFragmentManager.findFragmentById(binding.fcvWeather.id) as? WeatherFragment
        fragment?.updateWeather(weather)
    }
}