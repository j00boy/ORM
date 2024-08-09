package com.orm.ui.fragment.board

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orm.data.model.board.BoardList
import com.orm.data.model.club.Club
import com.orm.databinding.FragmentBoardAllBinding
import com.orm.ui.adapter.ProfileBoardAdapter
import com.orm.ui.board.BoardDetailActivity
import com.orm.viewmodel.BoardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BoardAllFragment : Fragment() {
    private var _binding: FragmentBoardAllBinding? = null
    private val binding get() = _binding!!

    private val boardViewModel: BoardViewModel by viewModels()

    private val rvBoard: RecyclerView by lazy { binding.recyclerView }
    private lateinit var adapter: ProfileBoardAdapter

    private val club: Club? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("club", Club::class.java)
        } else {
            arguments?.getParcelable<Club>("club")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentBoardAllBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val clubId = club?.id ?: -1

        // Fetch board list using clubId
        boardViewModel.getBoardList(clubId)
        boardViewModel.boardList.observe(viewLifecycleOwner) { boardList ->
            Log.e("BoardAllFragment", boardList.toString())
            setupAdapter(boardList, clubId)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupAdapter(boardList: List<BoardList>, clubId: Int) {
        Log.d("update123", "update123 : set")

        val reversedList = boardList.reversed()

        adapter = ProfileBoardAdapter(reversedList.map { BoardList.toRecyclerViewBoardItem(it) })

        adapter.setItemClickListener(object : ProfileBoardAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val intent = Intent(
                    requireContext(),
                    BoardDetailActivity::class.java
                ).apply {
                    putExtra("boardList", reversedList[position])
                    putExtra("club", club)
                }
                Log.e("boardAllFragment", "board: ${reversedList[position]}")
                startActivity(intent)
            }
        })

        rvBoard.adapter = adapter
        rvBoard.layoutManager = LinearLayoutManager(requireContext())
    }

    fun refreshData() {
        val clubId = club?.id ?: -1
        Log.d("update123", "update123 : $clubId")
        boardViewModel.getBoardList(clubId)
    }
}
