package com.orm.ui.fragment.club

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.orm.data.model.club.Club
import com.orm.databinding.FragmentClubEditBinding

class ClubEditFragment : Fragment() {
    private var _binding: FragmentClubEditBinding? = null
    private val binding get() = _binding!!
    private val club: Club? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            club = it.getParcelable("club")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentClubEditBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.cvImageUpload.setOnClickListener {
            // TODO : gallery open
            Toast.makeText(requireContext(), "갤러리 열기", Toast.LENGTH_SHORT).show()
        }

        binding.tfClubName.setEndIconOnClickListener {
            Toast.makeText(
                requireContext(),
                "모임명 중복 검사 ${binding.tfClubName.editText?.text.toString()}",
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
                .setMessage("모임을 생성/수정 하시겠습니까?")
                .setNegativeButton("취소") { _, _ -> }
                .setPositiveButton("확인") { dialog, which ->
                    // TODO : club 생성/수정
                    // TODO : move to this club detail
                    dialog.dismiss()
                }
                .show()
        }

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance(club: Club?) =
            ClubEditFragment().apply {
                arguments = Bundle().apply {
//                    putParcelable("club", club)
                }
            }
    }
}