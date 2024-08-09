package com.orm.ui.fragment.map

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.orm.R
import com.orm.data.model.Point
import com.orm.databinding.FragmentGoogleMapBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BasicGoogleMapFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentGoogleMapBinding? = null
    private val binding get() = _binding!!

    private var googleMap: GoogleMap? = null
    private var polyline: Polyline? = null
    private var points: List<Point> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            points = it.getParcelableArrayList(ARG_TRAIL) ?: emptyList()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentGoogleMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
        val southKoreaLatLng = LatLng(36.5, 127.5)
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(southKoreaLatLng, 7f))
        updateMap(points)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        Log.e(TAG, mapFragment.toString())
        mapFragment.getMapAsync(this)
    }

    private fun updateMap(points: List<Point>) {
        googleMap?.let { map ->
            val latLngPoints = points.map { LatLng(it.x, it.y) }

            if (latLngPoints.isNotEmpty()) {
                val startPoint = latLngPoints.first()
                val endPoint = latLngPoints.last()
                val centerPoint = LatLng(
                    (startPoint.latitude + endPoint.latitude) / 2,
                    (startPoint.longitude + endPoint.longitude) / 2
                )
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(centerPoint, 12.0f))
            }

            polyline?.remove()
            polyline = map.addPolyline(
                PolylineOptions()
                    .clickable(true)
                    .color(Color.RED)
                    .addAll(latLngPoints)
            )
        } ?: Log.e(TAG, "GoogleMap is not initialized")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        googleMap = null
        _binding = null
    }

    fun updatePoints(newPoints: List<Point>) {
        updateMap(newPoints)
    }

    companion object {
        private const val TAG = "GoogleMapFragment"
        private const val ARG_TRAIL = "trail"

        fun newInstance(points: List<Point>): BasicGoogleMapFragment {
            val fragment = BasicGoogleMapFragment()
            val args = Bundle().apply {
                putParcelableArrayList(ARG_TRAIL, ArrayList(points))
            }
            fragment.arguments = args
            return fragment
        }
    }
}
