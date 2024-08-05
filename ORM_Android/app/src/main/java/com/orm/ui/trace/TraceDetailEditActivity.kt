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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.orm.data.model.Trace
import com.orm.databinding.ActivityTraceDetailEditBinding
import com.orm.ui.fragment.map.BasicGoogleMapFragment
import com.orm.viewmodel.TraceViewModel
import com.orm.viewmodel.TrailViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@AndroidEntryPoint
class TraceDetailEditActivity : AppCompatActivity() {
    private val binding: ActivityTraceDetailEditBinding by lazy {
        ActivityTraceDetailEditBinding.inflate(layoutInflater)
    }

    private val traceViewModel: TraceViewModel by viewModels()
    private val trailViewModel: TrailViewModel by viewModels()

    private var imageUri: Uri? = null
    private var imagePath: String? = null

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

        binding.trace = trace

        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnSign.setOnClickListener {
            if (binding.tfTraceName.editText!!.text.isEmpty()) {
                MaterialAlertDialogBuilder(this)
                    .setTitle("경고")
                    .setMessage("발자국 이름을 입력하세요.")
                    .setPositiveButton("확인") { _, _ -> }
                    .show()
                return@setOnClickListener
            }

            MaterialAlertDialogBuilder(this)
                .setTitle("수정하기")
                .setMessage("발자국을 수정하시겠습니까?")
                .setNegativeButton("취소") { _, _ -> }
                .setPositiveButton("확인") { dialog, which ->
                    val traceModify = Trace(
                        id = trace?.id,
                        localId = trace?.localId ?: 0,
                        title = binding.tfTraceName.editText?.text.toString(),
                        hikingDate = trace!!.hikingDate,
                        hikingStartedAt = trace!!.hikingStartedAt,
                        hikingEndedAt = trace!!.hikingEndedAt,
                        mountainId = trace!!.mountainId,
                        mountainName = trace!!.mountainName,
                        coordinates = trace!!.coordinates,
                        trailId = trace!!.trailId,
                        maxHeight = trace!!.maxHeight,
                        imgPath = imagePath,
                    )
                    traceViewModel.createTrace(traceModify)
                    Log.d("traceTest", imagePath.toString())
                    dialog.dismiss()

                    binding.progressBar.visibility = View.VISIBLE
                    traceViewModel.traceCreated.observe(this) { traceCreated ->
                        if (traceCreated) {
                            binding.progressBar.visibility = View.GONE
                            setResult(Activity.RESULT_OK, Intent().apply {
                                putExtra("traceCreated", true)
                            })
                            finish()
                        }
                    }
                    finish()
                }
                .show()
        }

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
                data?.data?.let {uri ->
                    val file = File(cacheDir, "selected_image.png")
                    try {
                        contentResolver.openInputStream(uri)?.use { inputStream ->
                            FileOutputStream(file).use { outputStream ->
                                inputStream.copyTo(outputStream)
                            }
                        }
                        imagePath = file.absolutePath
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    binding.ivThumbnail.setImageURI(imageUri)
                    binding.ivThumbnail.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                }
            }
        }
}