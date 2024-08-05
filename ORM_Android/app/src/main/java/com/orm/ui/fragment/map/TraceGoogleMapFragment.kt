package com.orm.ui.fragment.map

import android.annotation.SuppressLint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.orm.R
import com.orm.data.model.Point
import com.orm.databinding.FragmentTraceGoogleMapBinding
import com.orm.util.LocationIntentService
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.pow
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import android.content.BroadcastReceiver as LocalReceiver

@AndroidEntryPoint
@SuppressLint("MissingPermission")
class TraceGoogleMapFragment : Fragment(), OnMapReadyCallback, SensorEventListener {

    private var _binding: FragmentTraceGoogleMapBinding? = null
    private val binding get() = _binding!!

    private val localBroadcastManager: LocalBroadcastManager by lazy {
        LocalBroadcastManager.getInstance(requireContext())
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var sensorManager: SensorManager
    private lateinit var pressureSensor: Sensor

    private var googleMap: GoogleMap? = null
    private var points: List<Point> = emptyList()
    private var polyline: Polyline? = null

    private var cnt : Int = 0

    private val locationReceiver = object : LocalReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            cnt++
            binding.tvTest.text = cnt.toString()

            val latitude = intent?.getDoubleExtra("latitude", 0.0) ?: 0.0
            val longitude = intent?.getDoubleExtra("longitude", 0.0) ?: 0.0
            Log.d("LocationReceiver", "Received location: $latitude, $longitude")
//            updateMapWithLocation(Location("").apply {
//                this.latitude = latitude
//                this.longitude = longitude
//            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeServices()

        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE) ?: run {
            Log.e(TAG, "Pressure sensor not available")
            return
        }

        arguments?.let {
            points = it.getParcelableArrayList(ARG_TRAIL) ?: emptyList()
            Log.e("TraceGoogleMapFragment", points.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTraceGoogleMapBinding.inflate(inflater, container, false)

        binding.btnStop.visibility = View.GONE

        binding.btnStart.setOnClickListener {
            Toast.makeText(requireContext(), "발자국 측정 시작", Toast.LENGTH_SHORT).show()
            binding.btnStart.visibility = View.GONE
            binding.btnStop.visibility = View.VISIBLE
            startLocationService()
        }

        binding.btnStop.setOnClickListener {
            Toast.makeText(requireContext(), "발자국 측정 종료", Toast.LENGTH_SHORT).show()
            stopLocationService()
        }

        return binding.root
    }

    private fun initializeServices() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    private fun fetchLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                Log.d("gmap", "Location fetched: $location")
                location?.let {
                    updateMapWithLocation(it)
                } ?: run {
                    Log.e("gmap", "Location not available")
                }
            }
            .addOnFailureListener { e ->
                Log.e("gmap", "Failed to get location", e)
            }
    }

    private fun startLocationService() {
        val intent = Intent(requireContext(), LocationIntentService::class.java)
        requireContext().startService(intent)
    }

    private fun stopLocationService() {
        val intent = Intent(requireContext(), LocationIntentService::class.java)
        requireContext().stopService(intent)
    }

    private fun updateMapWithLocation(location: Location) {
        googleMap?.let { map ->
            val currentLatLng = LatLng(location.latitude, location.longitude)
            map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(currentLatLng, 17f)
            )
        } ?: Log.e(TAG, "GoogleMap is not initialized")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        localBroadcastManager.registerReceiver(
            locationReceiver, IntentFilter("com.orm.LOCATION_UPDATE"),
        )
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap?.isMyLocationEnabled = true
        googleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
        updateMap(points)
        fetchLocation()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL)


    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
//        event?.let {
//            Log.d(TAG, "Pressure sensor values: ${calculateAltitude(it.values[0].toDouble())}m")
//        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Implement accuracy changes if needed
    }

    override fun onDestroyView() {
        super.onDestroyView()
        localBroadcastManager.unregisterReceiver(locationReceiver)
        googleMap = null
    }

    private fun updateMap(points: List<Point>) {
        Log.d(TAG, "Updating map with points: $points")

        googleMap?.let { map ->
            val latLngPoints = points.map { LatLng(it.x, it.y) }

            polyline?.remove()
            polyline = map.addPolyline(
                PolylineOptions()
                    .clickable(true)
                    .color(R.color.md_theme_errorContainer_mediumContrast)
                    .addAll(latLngPoints)
            )
        } ?: Log.e(TAG, "GoogleMap is not initialized")
    }

    private fun calculateAltitude(pressure: Double) =
        44330 * (1 - (pressure / 1013.25).pow(1 / 5.255))

    companion object {
        private const val TAG = "GoogleMapTraceFragment"
        private const val ARG_TRAIL = "trail"

        fun newInstance(points: List<Point>): TraceGoogleMapFragment {
            val fragment = TraceGoogleMapFragment()
            val args = Bundle().apply {
                putParcelableArrayList(ARG_TRAIL, ArrayList(points))
            }
            fragment.arguments = args
            return fragment
        }
    }
}
