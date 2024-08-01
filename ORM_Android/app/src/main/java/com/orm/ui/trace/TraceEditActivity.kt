package com.orm.ui.trace

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.orm.data.model.Mountain
import com.orm.data.model.Trace
import com.orm.databinding.ActivityTraceEditBinding
import com.orm.ui.fragment.BottomSheetMountainList
import com.orm.viewmodel.TraceViewModel
import dagger.hilt.android.AndroidEntryPoint
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

    private var mountainId: Int = 0
    private var mountainName: String = ""
    private val traceViewModel: TraceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.trace = trace

        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.tfTraceMountain.setEndIconOnClickListener {
            Log.d("TraceEditActivity", binding.tfTraceMountain.editText?.text.toString())

            val mountainName = binding.tfTraceMountain.editText?.text.toString()
            val bottomSheetFragment =
                BottomSheetMountainList.newInstance(mountainName)
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }

        binding.btnSign.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("생성하기 / 수정하기")
                .setMessage("발자국을 생성/수정 하시겠습니까?")
                .setNegativeButton("취소") { _, _ -> }
                .setPositiveButton("확인") { dialog, which ->
                    // TODO : trace 수정
                    val traceCreate = Trace(
                        id = null,
                        title = binding.tfTraceName.editText?.text.toString(),
                        hikingDate = binding.tfDate.editText?.text.toString(),
                        mountainId = mountainId,
                        mountainName = mountainName,
                        coordinates = null,
                        trailId = 1,
                    )
                    traceViewModel.createTrace(traceCreate)
                    dialog.dismiss()
                    finish()
                }
                .show()
        }

        val picker =
            MaterialDatePicker.Builder.datePicker()
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

    override fun onMountainSelected(mountain: Mountain) {
        binding.tfTraceMountain.editText?.setText(mountain.name)
        mountainId = mountain.id
        mountainName = mountain.name
    }
}