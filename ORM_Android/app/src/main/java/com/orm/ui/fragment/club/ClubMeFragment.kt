package com.orm.ui.fragment.club

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.orm.data.model.RecyclerViewItem
import com.orm.databinding.FragmentClubMeBinding
import com.orm.ui.adapter.ItemMainAdapter

class ClubMeFragment : Fragment() {
    private var _binding: FragmentClubMeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentClubMeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val rvBoard = binding.recyclerView
        val adapter = ItemMainAdapter(
            listOf(
                RecyclerViewItem(
                    imageSrc = "https://img.tenping.kr/Content/Upload/Images/2023021311020002_Dis_20230223085848.jpg?RS=180x120",
                    title = "안녕하세요.",
                    subTitle = "반갑습니다."
                ),
                RecyclerViewItem(
                    imageSrc = "http://via.placeholder.com/300.png",
                    title = "asdfghjkl",
                    subTitle = "iuytre1243"
                ),
                RecyclerViewItem(
                    imageSrc = "https://img.tenping.kr/Content/Upload/Images/2023021311020002_Dis_20230223085848.jpg?RS=180x120",
                    title = "안녕하세요.",
                    subTitle = "반갑습니다."
                ),
                RecyclerViewItem(
                    imageSrc = "http://via.placeholder.com/300.png",
                    title = "asdfghjkl",
                    subTitle = "iuytre1243"
                ),
                RecyclerViewItem(
                    imageSrc = "https://img.tenping.kr/Content/Upload/Images/2023021311020002_Dis_20230223085848.jpg?RS=180x120",
                    title = "안녕하세요.",
                    subTitle = "반갑습니다."
                ),
                RecyclerViewItem(
                    imageSrc = "http://via.placeholder.com/300.png",
                    title = "asdfghjkl",
                    subTitle = "iuytre1243"
                ),
                RecyclerViewItem(
                    imageSrc = "https://img.tenping.kr/Content/Upload/Images/2023021311020002_Dis_20230223085848.jpg?RS=180x120",
                    title = "안녕하세요.",
                    subTitle = "반갑습니다."
                ),
                RecyclerViewItem(
                    imageSrc = "http://via.placeholder.com/300.png",
                    title = "asdfghjkl",
                    subTitle = "iuytre1243"
                ),
            )
        )

        adapter.setItemClickListener(object : ItemMainAdapter.OnItemClickListener {
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