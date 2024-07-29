package com.orm.ui.fragment.club

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.orm.data.model.recycler.RecyclerViewBasicItem
import com.orm.databinding.FragmentClubAllBinding
import com.orm.ui.adapter.ProfileBasicAdapter

class ClubAllFragment : Fragment() {
    private var _binding: FragmentClubAllBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentClubAllBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val rvBoard = binding.recyclerView
        val adapter = ProfileBasicAdapter(
            listOf(
                RecyclerViewBasicItem(
                    imageSrc = "https://img.tenping.kr/Content/Upload/Images/2023021311020002_Dis_20230223085848.jpg?RS=180x120",
                    title = "안녕하세요.",
                    subTitle = "반갑습니다."
                ),
            )
        )

        adapter.setItemClickListener(object : ProfileBasicAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {}
        })

        rvBoard.adapter = adapter
        rvBoard.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}