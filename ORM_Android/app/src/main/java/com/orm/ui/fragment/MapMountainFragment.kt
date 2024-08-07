package com.orm.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import com.orm.R
import com.orm.data.model.Mountain
import com.orm.databinding.FragmentMapMountainBinding
import com.orm.ui.mountain.MountainDetailActivity
import com.orm.viewmodel.MountainViewModel
import dagger.hilt.android.AndroidEntryPoint

// TODO : 마커 이미지, 인포윈도우 UI 변경
@AndroidEntryPoint
class MapMountainFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapMountainBinding? = null
    private val binding get() = _binding!!

    private lateinit var clusterManager: ClusterManager<MyItem>
    private var googleMap: GoogleMap? = null
    private var mountains: List<Mountain>? = null
    private val mountainMap = mutableMapOf<Int, Mountain>()

    private val mountainViewModel: MountainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapMountainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL

        val uiSettings = googleMap?.uiSettings
        uiSettings?.isCompassEnabled = true
        uiSettings?.isZoomControlsEnabled = true
        uiSettings?.isMapToolbarEnabled = false
        uiSettings?.isMyLocationButtonEnabled = true

        setUpClusterer()
        observeMountains()
    }

    override fun onDestroy() {
        super.onDestroy()
        googleMap = null
        _binding = null
    }

    private fun observeMountains() {
        mountainViewModel.fetchMountainByName("")
        mountainViewModel.mountains.observe(viewLifecycleOwner) { mountainList ->
            if (mountainList != null) {
                mountains = mountainList
                mountainMap.clear()
                mountains?.forEach { mountain ->
                    mountainMap[mountain.id] = mountain
                }
                addItems()
            }
        }
    }

    private fun setUpClusterer() {
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(37.5665, 126.9780), 10f))

        clusterManager = ClusterManager(context, googleMap)
        googleMap?.setOnCameraIdleListener(clusterManager)

        clusterManager.setOnClusterClickListener { cluster ->
            googleMap?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    cluster.position,
                    googleMap?.cameraPosition?.zoom?.plus(2) ?: 15f
                )
            )
            true
        }

        clusterManager.setOnClusterItemInfoWindowClickListener { item ->
            val mountain = mountainMap[item.getId()]
            mountain?.let {
                val intent = Intent(requireContext(), MountainDetailActivity::class.java)
                intent.putExtra("mountain", it)
                startActivity(intent)
            }
        }
    }

    private fun addItems() {
        mountains?.forEach { mountain ->
            val offsetItem = MyItem(
                mountain.id,
                mountain.addressLatitude,
                mountain.addressLongitude,
                mountain.name,
                mountain.height.toString() + "m",
            )
            clusterManager.addItem(offsetItem)
        }
        clusterManager.cluster()
    }

    inner class MyItem(
        private val id: Int,
        lat: Double,
        lng: Double,
        private val title: String,
        private val snippet: String,
    ) : ClusterItem {

        private val position: LatLng = LatLng(lat, lng)

        override fun getPosition(): LatLng = position
        override fun getTitle(): String = title
        override fun getSnippet(): String = snippet
        fun getId(): Int = id
    }
}
