package com.orm.ui.trace

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.orm.R
import com.orm.data.model.Trace
import com.orm.databinding.ActivityTraceDetailBinding
import com.orm.ui.fragment.map.BasicGoogleMapFragment
import com.orm.util.uriToFile
import com.orm.viewmodel.TraceViewModel
import com.orm.viewmodel.TrailViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class TraceDetailEditActivity : AppCompatActivity() {
    private val binding: ActivityTraceDetailBinding by lazy {
        ActivityTraceDetailBinding.inflate(layoutInflater)
    }
    private val traceViewModel: TraceViewModel by viewModels()
    private val trailViewModel: TrailViewModel by viewModels()

    private var imageUri: Uri? = null
    private var imageFile: File? = null

    private val createTraceLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val traceCreated = data?.getBooleanExtra("traceCreated", false) ?: false
                if (traceCreated) {
                    traceViewModel.getTraces()
                }
            }
        }

    private val trace: Trace? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("trace", Trace::class.java)
        } else {
            intent.getParcelableExtra<Trace>("trace")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.fcvMap.id, BasicGoogleMapFragment())
                .commit()
        }

        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnStart.setOnClickListener {
            val intent = Intent(this, TraceMeasureActivity::class.java)
            startActivity(intent)
        }

        binding.trace = trace

        if (trace!!.trailId == -1) {
            binding.cvMap.visibility = View.GONE
        } else {
            trailViewModel.getTrail(trace!!.trailId!!)
            trailViewModel.trail.observe(this@TraceDetailEditActivity) {
                val fragment =
                    supportFragmentManager.findFragmentById(binding.fcvMap.id) as? BasicGoogleMapFragment
                fragment?.updatePoints(it.trailDetails)
            }
        }

        if (trace!!.hikingStartedAt.isNullOrEmpty()) {
            binding.fcvTable.visibility = View.GONE
        } else {
            binding.btnStart.visibility = View.GONE
        }

        binding.cvImageUpload.setOnClickListener {
            openGallery()
        }
    }

    private fun openGallery() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        pickImageLauncher.launch(gallery)
    }

    private val pickImageLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                data?.data?.let {
                    imageUri = it
                    binding.ivThumbnail.setImageURI(imageUri)
                    binding.ivThumbnail.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                    imageFile = imageUri?.let { uriToFile(it, contentResolver) }
                }
            }
        }
}