package com.orm.ui.fragment.map

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.orm.R
import com.orm.data.model.Point
import com.orm.data.repository.MountainRepository
import com.orm.databinding.FragmentGoogleMapBinding
import com.orm.viewmodel.MountainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.pow

@AndroidEntryPoint
class GoogleMapFragment : Fragment(), OnMapReadyCallback, SensorEventListener {

    private lateinit var binding: FragmentGoogleMapBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationManager: LocationManager
    private lateinit var sensorManager: SensorManager
    private lateinit var googleMap: GoogleMap
    private lateinit var pressureSensor: Sensor
    private val mountainViewModel: MountainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_google_map, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeServices()

        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE) ?: run {
            Log.e(TAG, "Pressure sensor not available")
            return
        }
    }

    private fun updateMap(points: List<Point>) {
        val latLngPoints = points.map { LatLng(it.latitude, it.longitude) }

        // Polyline 추가
        val polyline = googleMap.addPolyline(
            PolylineOptions()
                .clickable(true)
                .addAll(latLngPoints)
        )

        // 지도 카메라를 첫 번째 점으로 이동
        if (latLngPoints.isNotEmpty()) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngPoints[0], 15f))
        }
    }

    private fun initializeServices() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

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
            Log.d(TAG, "Pressure sensor values: ${calculateAltitude(it.values[0].toDouble())}m")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Implement accuracy changes if needed
    }

    private val locationListener = LocationListener { location ->
        Log.d(TAG, "Location changed: $location")
        updateMapWithLocation(location)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // ViewModel에서 데이터를 가져오도록 요청
        mountainViewModel.fetchTrailById(126)

        // LiveData 관찰
        mountainViewModel.points.observe(viewLifecycleOwner, Observer { points ->
            if (points != null) {
                updateMap(points)
            }
        })
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
//        fetchLocationByDevice()
    }

    @SuppressLint("MissingPermission")
    private fun fetchLocationByDevice() {
        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        location?.let {
            updateMapWithLocation(it)
        } ?: run {
            Log.e(TAG, "Location not available")
        }

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            1000L,
            1f,
            locationListener
        )
    }

    private fun updateMapWithLocation(location: Location) {
        val currentLatLng = LatLng(location.latitude, location.longitude)
        googleMap.clear()
        googleMap.addMarker(MarkerOptions().position(currentLatLng).title("현재 위치"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 17f))
        Log.d(TAG, "Current location: $currentLatLng, Altitude: ${location.altitude}")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        locationManager.removeUpdates(locationListener)
    }

    companion object {
        private const val TAG = "GoogleMapFragment"
    }

    private fun calculateAltitude(pressure: Double) = 44330 * (1 - (pressure / 1013.25).pow(1 / 5.255))

}
