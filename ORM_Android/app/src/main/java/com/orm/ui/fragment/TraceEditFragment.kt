package com.orm.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.orm.data.model.club.Club
import com.orm.databinding.FragmentTraceEditBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TraceEditFragment : Fragment() {
    private var _binding: FragmentTraceEditBinding? = null
    private val binding get() = _binding!!
    private val trace: Club? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            trace = it.getParcelable("trace")
        }
    }

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTraceEditBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.tfClubName.setEndIconOnClickListener {
            Toast.makeText(
                requireContext(),
                "발자국 이름 ${binding.tfClubName.editText?.text.toString()}",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.tfClubMountain.setEndIconOnClickListener {
            Toast.makeText(
                requireContext(),
                "산 검색 ${binding.tfClubMountain.editText?.text.toString()}",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.btnSign.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("생성하기 / 수정하기")
                .setMessage("발자국을 생성/수정 하시겠습니까?")
                .setNegativeButton("취소") { _, _ -> }
                .setPositiveButton("확인") { dialog, which ->
                    // TODO : trace 생성/수정
                    // TODO : move to this trace detail
                    dialog.dismiss()
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
            picker.show(parentFragmentManager, "trace_date")
        }

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TraceEditFragment().apply {
                TraceEditFragment().apply {
                    arguments = Bundle().apply {
//                    putParcelable("trace", trace)
                    }
                }
            }
    }
}