package com.orm.ui.fragment.club

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
                    imageSrc = "https://lh5.googleusercontent.com/p/AF1QipN6Z5jI8aSdC5YBW3W2ebuXk1jyGGMAoTB3h6Mi=w675-h390-n-k-no",
                    title = "설악산 등반",
                    subTitle = "맑은 날씨에 다녀온 설악산 등반기"
                ),
                RecyclerViewItem(
                    imageSrc = "https://encrypted-tbn2.gstatic.com/licensed-image?q=tbn:ANd9GcQFH7DYDI9Wh9Zcofd6iOKsQCWeCxy2ms0mifHKIryJW5toYEFhyfUwAh-tusoQFVWe1H2HE4X711i4AUmDqoWCGlJ0WwZWY8TiIlBnE9g",
                    title = "지리산 트레킹",
                    subTitle = "지리산 둘레길을 걸으며"
                ),
                RecyclerViewItem(
                    imageSrc = "https://lh5.googleusercontent.com/p/AF1QipOv8rAg_SFGkLgJOmWjIEpkLe2VmpgPYFiqWppg=w675-h390-n-k-no",
                    title = "북한산 산행",
                    subTitle = "도심 속의 자연, 북한산 산행기"
                ),
                RecyclerViewItem(
                    imageSrc = "https://encrypted-tbn3.gstatic.com/licensed-image?q=tbn:ANd9GcQKU-ao0qdpV2QqhHWoBhbtYOxtqBrLm6Lnl8F2VrScEcu5Kuaz5ZA8_KFvN822ApHJX1ZOGksTYOgw9xnlSbH4lU0fhhyLNrFSKzYGJg",
                    title = "한라산 겨울 산행",
                    subTitle = "눈 덮인 한라산의 아름다움"
                ),
                RecyclerViewItem(
                    imageSrc = "https://encrypted-tbn2.gstatic.com/licensed-image?q=tbn:ANd9GcSwnVvAoHEY2JGGbLY8ulacnUvD9t6WeTc3-985FZjoHzPhMWLS1yisfTYYBzYyrjizy5l5UgiC1GlJahxCbP1ZqsFmP3bDtiw71N8ueQ",
                    title = "속리산 등반",
                    subTitle = "속리산의 경치를 만끽하며"
                ),
                RecyclerViewItem(
                    imageSrc = "https://www.example.com/images/mountain6.jpg",
                    title = "덕유산 산행",
                    subTitle = "가을의 덕유산을 오르다"
                ),
                RecyclerViewItem(
                    imageSrc = "https://www.example.com/images/mountain7.jpg",
                    title = "치악산 봄 산행",
                    subTitle = "봄꽃이 만개한 치악산"
                ),
                RecyclerViewItem(
                    imageSrc = "https://www.example.com/images/mountain8.jpg",
                    title = "오대산 단풍 산행",
                    subTitle = "가을 단풍이 절경인 오대산"
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
