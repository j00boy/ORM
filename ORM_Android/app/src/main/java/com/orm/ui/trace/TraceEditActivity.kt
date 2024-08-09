package com.orm.ui.trace

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.orm.data.model.Mountain
import com.orm.data.model.Point
import com.orm.data.model.Trace
import com.orm.data.model.Trail
import com.orm.databinding.ActivityTraceEditBinding
import com.orm.ui.fragment.BottomSheetMountainList
import com.orm.ui.fragment.map.BasicGoogleMapFragment
import com.orm.viewmodel.MountainViewModel
import com.orm.viewmodel.TraceViewModel
import com.orm.viewmodel.TrailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class TraceEditActivity : AppCompatActivity(), BottomSheetMountainList.OnMountainSelectedListener {
    private val binding: ActivityTraceEditBinding by lazy {
        ActivityTraceEditBinding.inflate(layoutInflater)
    }

    private val trace: Trace? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("trace", Trace::class.java)
        } else {
            intent.getParcelableExtra<Trace>("trace")
        }
    }

    private val trailIndex: Int by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getIntExtra("trailIndex", 0)
        } else {
            intent.getIntExtra("trailIndex", 0)
        }
    }

    private val mountain: Mountain? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("mountain", Mountain::class.java)
        } else {
            intent.getParcelableExtra<Mountain>("mountain")
        }
    }

    private var mountainId: Int = -1
    private var mountainName: String = "선택된 산 없음"
    private var trails: List<Trail> = emptyList()

    private val traceViewModel: TraceViewModel by viewModels()
    private val mountainViewModel: MountainViewModel by viewModels()
    private val trailViewModel: TrailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.fcvMap.id, BasicGoogleMapFragment())
                .commit()
        }

        if (trace != null) {
            if(trace!!.trailId != null) {
                trailViewModel.getTrail(trace!!.trailId!!)
            }
            trailViewModel.trail.observe(this@TraceEditActivity) {
                updateMapFragment(it.trailDetails)
            }
            mountainId = trace!!.mountainId
            mountainName = if(mountainId == -1) "" else trace!!.mountainName!!
            binding.mountainName = mountainName
        }

        binding.trace = trace

        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        if ((trace != null && trace!!.trailId == -1) || trace == null) {
            if (mountain == null) {
                binding.cvMap.visibility = View.GONE
                binding.spinnerTrails.visibility = View.GONE
            }
        }
        if (mountain != null) {
            mountainId = mountain!!.id
            mountainName = mountain!!.name
            binding.mountainName = mountainName
            mountainViewModel.fetchMountainById(mountainId)
            mountainViewModel.mountain.observe(this@TraceEditActivity) { it ->
                if(it?.trails?.size != 0) {
                    setupTrailSpinner(it?.trails!!)
                    this.trails = it.trails
                    binding.spinnerTrails.setSelection(trailIndex)
                } else {
                    binding.cvMap.visibility = View.GONE
                    binding.spinnerTrails.visibility = View.GONE
                }
            }

        }

        binding.tfTraceMountain.setEndIconOnClickListener {
            val mountainName = binding.tfTraceMountain.editText?.text.toString()
            val bottomSheetFragment = BottomSheetMountainList.newInstance(mountainName)
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
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

            val content = if (trace != null) "수정" else "생성"
            MaterialAlertDialogBuilder(this)
                .setTitle("${content}하기")
                .setMessage("발자국을 $content 하시겠습니까?")
                .setNegativeButton("취소") { _, _ -> }
                .setPositiveButton("확인") { dialog, which ->

                    val selectedTrailIndex = binding.spinnerTrails.selectedItemPosition
                    var selectedTrail = if (selectedTrailIndex != AdapterView.INVALID_POSITION) {
                        trails[selectedTrailIndex]
                    } else {
                        null
                    }

                    if (selectedTrail == null && trace?.trailId != null) {

                        trailViewModel.getTrail(trace!!.trailId!!)
                        trailViewModel.trail.observe(this@TraceEditActivity) {
                            selectedTrail = it
                        }
                    }

                    val traceCreate = Trace(
                        localId = trace?.localId ?: 0,
                        id = trace?.id,
                        title = binding.tfTraceName.editText?.text.toString(),
                        hikingDate = binding.tfDate.editText?.text.toString(),
                        mountainId = mountainId,
                        mountainName = mountainName,
                        trailId = selectedTrail?.id,
                        coordinates = selectedTrail?.trailDetails,
                    )

                    traceViewModel.createTrace(traceCreate)
                    if(selectedTrail != null) {
                        trailViewModel.createTrail(selectedTrail!!)
                    }

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
                }
                .show()
        }

        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("등산 날짜 선택")
            .setTextInputFormat(SimpleDateFormat("yyyy-MM-dd"))
            .build()

        picker.addOnPositiveButtonClickListener {
            val selectedDateInMillis = it
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dateString = sdf.format(Date(selectedDateInMillis))
            binding.tfDate.editText?.setText(dateString)
        }

        binding.tfDate.setStartIconOnClickListener {
            picker.show(supportFragmentManager, "trace_date")
        }
    }

    private fun updateMapFragment(points: List<Point>) {
        val fragment =
            supportFragmentManager.findFragmentById(binding.fcvMap.id) as? BasicGoogleMapFragment
        fragment?.updatePoints(points)
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
                Log.e("MountainDetailActivity", selectedTrail.trailDetails.toString())
                updateMapFragment(selectedTrail.trailDetails)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    override fun onMountainSelected(mountain: Mountain) {
        binding.tfTraceMountain.editText?.setText(mountain.name)
        mountainId = mountain.id
        mountainName = mountain.name

        binding.cvMap.visibility = View.VISIBLE
        binding.spinnerTrails.visibility = View.VISIBLE

        mountainViewModel.fetchMountainById(mountainId)
        mountainViewModel.mountain.observe(this@TraceEditActivity) { it ->
            it?.trails?.let { trails ->
                setupTrailSpinner(trails)
                this.trails = trails
            }
        }
    }
}
