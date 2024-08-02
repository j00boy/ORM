package com.orm.ui.fragment.map

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
import com.orm.databinding.FragmentGoogleMapBinding
import com.orm.viewmodel.MountainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BasicGoogleMapFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentGoogleMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var googleMap: GoogleMap? = null
    private val mountainViewModel: MountainViewModel by viewModels()

    private var points: List<Point> = emptyList()
    private var polyline: Polyline? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
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
        updateMap(points)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun updateMap(points: List<Point>) {
        googleMap?.let { map ->
            val latLngPoints = points.map { LatLng(it.latitude, it.longitude) }

            // 기존의 폴리라인 삭제
            polyline?.remove()

            if (latLngPoints.isNotEmpty()) {
                val startPoint = latLngPoints.first()
                val endPoint = latLngPoints.last()
                val centerPoint = LatLng(
                    (startPoint.latitude + endPoint.latitude) / 2,
                    (startPoint.longitude + endPoint.longitude) / 2
                )
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(centerPoint, 12.0f))
            }

            // 새로운 폴리라인 추가
            polyline = map.addPolyline(
                PolylineOptions()
                    .clickable(true)
                    .color(R.color.md_theme_errorContainer_mediumContrast)
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
        points = newPoints
        updateMap(points)
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
