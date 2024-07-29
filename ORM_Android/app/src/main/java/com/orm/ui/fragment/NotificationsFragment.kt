package com.orm.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.orm.data.model.recycler.RecyclerViewNotificationItem
import com.orm.databinding.FragmentNotificationsBinding
import com.orm.ui.adapter.ProfileNotificationAdapter

class NotificationsFragment : Fragment() {
    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private val temp = listOf(
        RecyclerViewNotificationItem(
            imageSrc = "https://img.tenping.kr/Content/Upload/Images/2023021311020002_Dis_20230223085848.jpg?RS=180x120",
            title = "안녕하세요.",
            subTitle = "반갑습니다.",
            date = "2024.07.07",
        ),
        RecyclerViewNotificationItem(
            imageSrc = "https://img.tenping.kr/Content/Upload/Images/2023021311020002_Dis_20230223085848.jpg?RS=180x120",
            title = "안녕하세요.",
            subTitle = "반갑습니다.",
            date = "2024.07.07",
        ),
        RecyclerViewNotificationItem(
            imageSrc = "https://img.tenping.kr/Content/Upload/Images/2023021311020002_Dis_20230223085848.jpg?RS=180x120",
            title = "안녕하세요.",
            subTitle = "반갑습니다.",
            date = "2024.07.07",
        ),
        RecyclerViewNotificationItem(
            imageSrc = "https://img.tenping.kr/Content/Upload/Images/2023021311020002_Dis_20230223085848.jpg?RS=180x120",
            title = "안녕하세요.",
            subTitle = "반갑습니다.",
            date = "2024.07.07",
        ),
        RecyclerViewNotificationItem(
            imageSrc = "https://img.tenping.kr/Content/Upload/Images/2023021311020002_Dis_20230223085848.jpg?RS=180x120",
            title = "안녕하세요.",
            subTitle = "반갑습니다.",
            date = "2024.07.07",
        ),
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.lifecycleOwner = viewLifecycleOwner
        binding.count = temp.count()

        val rvBoard = binding.recyclerView
        val adapter = ProfileNotificationAdapter(temp)

        adapter.setItemClickListener(object : ProfileNotificationAdapter.OnItemClickListener {
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