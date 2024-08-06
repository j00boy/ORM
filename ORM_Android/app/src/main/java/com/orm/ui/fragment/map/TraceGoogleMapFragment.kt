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
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.orm.viewmodel.RecordViewModel
import com.orm.viewmodel.TrackViewModel
import com.orm.data.model.Record
import com.orm.data.model.Trace
import com.orm.util.localDateTimeToLong
import com.orm.viewmodel.TraceViewModel
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import android.content.BroadcastReceiver as LocalReceiver

@AndroidEntryPoint
@SuppressLint("MissingPermission")
class TraceGoogleMapFragment : Fragment(), OnMapReadyCallback, SensorEventListener {

    private var _binding: FragmentTraceGoogleMapBinding? = null
    private val binding get() = _binding!!

    private val localBroadcastManager: LocalBroadcastManager by lazy {
        LocalBroadcastManager.getInstance(requireContext())
    }

    private val trackViewModel: TrackViewModel by viewModels()
    private val recordViewModel: RecordViewModel by viewModels()
    private val traceViewModel: TraceViewModel by viewModels()

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var sensorManager: SensorManager
    private lateinit var pressureSensor: Sensor
    private lateinit var updateTimeRunnable: Runnable

    private var googleMap: GoogleMap? = null
    private var points: List<Point> = emptyList()
    private var polyline: Polyline? = null
    private var userPolyline: Polyline? = null
    private var currentHeight: Double? = null
    private var traceId: Int? = null

    private val handler = Handler(Looper.getMainLooper())
    private val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.KOREA)
    private val timeZone = TimeZone.getTimeZone("")
    private var startTime: Long = 0
    private var elapsedTime: Long = 0
    private var running = false

    private val locationReceiver = object : LocalReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val latitude = intent?.getDoubleExtra("latitude", 0.0) ?: 0.0
            val longitude = intent?.getDoubleExtra("longitude", 0.0) ?: 0.0
            Log.d("LocationReceiver", "Received location: $latitude, $longitude")
            updateMapWithLocation(LatLng(latitude, longitude))
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
            points = it.getParcelableArrayList(ARG_POINT) ?: emptyList()
            traceId = it.getInt(ARG_TRACE_ID)
        }

        dateFormat.timeZone = timeZone
        updateTimeRunnable = object : Runnable {
            override fun run() {
                updateCurrentTime()
                handler.postDelayed(this, 1000)
            }
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
            startStopwatch()
        }

        binding.btnStop.setOnClickListener {
            trackViewModel.points.value?.let { points ->
                Log.e(TAG, "Points: $points")
                insertRecordAndHandleTrace(points)
            }
            stopLocationService()
        }
        return binding.root
    }

    //    private fun insertRecordAndHandleTrace(points: List<Point>) {
//        recordViewModel.insertRecord(Record(coordinate = points))
//        recordViewModel.recordId.observe(requireActivity()) { createdId ->
//            Log.e(TAG, "Record created with ID: $createdId")
//            Log.e(TAG, "Trace ID: $traceId")
//            if (traceId != null && traceId != -1) {
//                traceViewModel.getTrace(traceId!!)
//                traceViewModel.trace.observe(requireActivity()) { trace ->
//                    Log.e(TAG, "Trace retrieved: $trace")
//                    handleTraceUpdate(trace, createdId)
//                }
//            }
//        }
//    }
    private fun insertRecordAndHandleTrace(points: List<Point>) {
        recordViewModel.insertRecord(Record(coordinate = points))
        recordViewModel.recordId.observe(requireActivity()) { createdId ->
            Log.e(TAG, "Record created with ID: $createdId")
            if (traceId != null && traceId != -1) {
                traceViewModel.getTrace(traceId!!)
                traceViewModel.trace.observe(requireActivity()) { trace ->
                    Log.e(TAG, "Trace retrieved: $trace")
                    trace?.let {
                        it.recordId = createdId
                        traceViewModel.createTrace(it)
                        Log.e(TAG, "Trace updated: ${it.recordId}")
                        traceViewModel.traceCreated.observe(requireActivity()) {
                            if (it) {
                                Toast.makeText(requireContext(), "발자국 측정 완료", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                }
            }
        }
    }


//    private fun handleTraceUpdate(trace: Trace?, createdId: Long) {
//        trace?.let {
//            trace.recordId = createdId
//            traceViewModel.createTrace(it)
//            Log.e(TAG, "Trace updated: ${it.recordId}")
//        }
//    }

    private fun initializeServices() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    private fun fetchLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                Log.d("gmap", "Location fetched: $location")
                location?.let {
                    updateMapWithLocation(LatLng(it.latitude, it.longitude))
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

    private fun updateMapWithLocation(latlng: LatLng) {
        googleMap?.let { map ->
            map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(latlng, map.cameraPosition.zoom)
            )

            if (!running) return

            Log.e(TAG, LocalDateTime.now().toString())
            Log.e(
                TAG, Point(
                    x = latlng.latitude,
                    y = latlng.longitude,
                    altitude = currentHeight,
                    time = localDateTimeToLong(LocalDateTime.now())
                ).toString()
            )

            trackViewModel.updatePoint(
                Point(
                    x = latlng.latitude,
                    y = latlng.longitude,
                    altitude = currentHeight,
                    time = localDateTimeToLong(LocalDateTime.now())
                )
            )
            trackViewModel.distance.observe(requireActivity()) {
                binding.distance = String.format("%.0f", it) + "m"
            }

            binding.altitude = String.format("%.0f", currentHeight) + "m"

            trackViewModel.points.observe(requireActivity()) {
                val positions = it.map { pos ->
                    LatLng(pos.x, pos.y)
                }
                userPolyline?.remove()
                userPolyline = map.addPolyline(
                    PolylineOptions()
                        .zIndex(10000000.0f)
                        .color(R.color.md_theme_inversePrimary)
                        .addAll(positions)
                )
            }
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
        googleMap?.moveCamera(CameraUpdateFactory.zoomTo(17f))

        fetchLocation()
        initializeMap(points)
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
        event?.let {
            if (it.sensor.type == Sensor.TYPE_PRESSURE) {
                currentHeight = calculateAltitude(it.values[0])
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Implement accuracy changes if needed
    }

    override fun onDestroyView() {
        super.onDestroyView()
        localBroadcastManager.unregisterReceiver(locationReceiver)
        trackViewModel.clearPoints()
        handler.removeCallbacks(updateTimeRunnable)
        googleMap = null
        _binding = null
    }

    private fun calculateAltitude(pressure: Float): Double =
        44330 * (1 - (pressure / 1013.25).pow(1 / 5.255))

    private fun updateCurrentTime() {
        if (running) {
            val currentTime = System.currentTimeMillis()
            val timeInMillis = elapsedTime + (currentTime - startTime)
            binding.time = dateFormat.format(Date(timeInMillis))
        }
    }

    private fun startStopwatch() {
        if (!running) {
            running = true
            startTime = System.currentTimeMillis()
            updateTimeRunnable.run()
        }
    }

    private fun initializeMap(points: List<Point>) {
        Log.d(TAG, "Updating map with points: $points")

        googleMap?.let { map ->
            val latLngPoints = points.map { LatLng(it.x, it.y) }

            polyline?.remove()
            polyline = map.addPolyline(
                PolylineOptions()
                    .color(R.color.md_theme_errorContainer_mediumContrast)
                    .addAll(latLngPoints)
            )
        } ?: Log.e(TAG, "GoogleMap is not initialized")
    }

    companion object {
        private const val TAG = "GoogleMapTraceFragment"
        private const val ARG_POINT = "point"
        private const val ARG_TRACE_ID = "trace_id"

        fun newInstance(points: List<Point>, traceId: Int? = null): TraceGoogleMapFragment {
            val fragment = TraceGoogleMapFragment()
            val args = Bundle().apply {
                putParcelableArrayList(ARG_POINT, ArrayList(points))
                putInt(ARG_TRACE_ID, traceId ?: -1)
            }
            fragment.arguments = args
            return fragment
        }
    }
}
