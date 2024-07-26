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
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.orm.R
import com.orm.databinding.FragmentGoogleMapBinding

class GoogleMapFragment : Fragment(R.layout.fragment_google_map), OnMapReadyCallback,
    SensorEventListener {

    private lateinit var binding: FragmentGoogleMapBinding
    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationManager: LocationManager
    private lateinit var sensorManager: SensorManager
    private lateinit var pressureSensor: Sensor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE) ?: run {
            Log.e("GoogleMapFragment", "Pressure sensor not available")
            return
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(
            this,
            pressureSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            Log.d("gmap1", "Pressure sensor values: x=${it.values[0]}")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Implement accuracy changes if needed
    }

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            val currentLatLng = LatLng(location.latitude, location.longitude)
            googleMap.clear()  // Clear previous markers
            googleMap.addMarker(MarkerOptions().position(currentLatLng).title("현재 위치"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 17f))
            Log.e("gmap", currentLatLng.toString())
            Log.e("gmap", location.altitude.toString())
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_google_map, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        fetchLocationByDevice()
    }

    @SuppressLint("MissingPermission")
    private fun fetchLocationByDevice() {
        val location: Location? = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        location?.let {
            val currentLatLng = LatLng(it.latitude, it.longitude)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 17f))
            googleMap.addMarker(MarkerOptions().position(currentLatLng).title("현재 위치"))
            Log.e("gmap", currentLatLng.toString())
            Log.e("gmap", it.altitude.toString())
        } ?: run {
            Log.e("gmap", "Location not available")
        }

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            1000L,
            1f,
            locationListener
        )
    }

    @SuppressLint("MissingPermission")
    private fun fetchLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    val currentLatLng = LatLng(it.latitude, it.longitude)
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 17f))
                    googleMap.addMarker(MarkerOptions().position(currentLatLng).title("한라산"))
                    Log.e("gmap", currentLatLng.toString())
                    Log.e("gmap", location.altitude.toString())
                } ?: run {
                    Log.e("gmap", "Location not available")
                }
            }
            .addOnFailureListener { e ->
                Log.e("gmap", "Failed to get location", e)
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        locationManager.removeUpdates(locationListener)
    }
}
