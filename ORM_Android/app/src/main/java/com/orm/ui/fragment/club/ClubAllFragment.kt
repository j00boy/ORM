package com.orm.ui.fragment.club

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orm.data.model.club.Club
import com.orm.data.model.recycler.RecyclerViewBasicItem
import com.orm.databinding.FragmentClubAllBinding
import com.orm.databinding.FragmentClubMeBinding
import com.orm.ui.adapter.ProfileBasicAdapter
import com.orm.ui.club.ClubDetailActivity
import com.orm.viewmodel.ClubViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClubAllFragment : Fragment() {
    private var _binding: FragmentClubAllBinding? = null
    private val binding get() = _binding!!

    private val clubViewModel: ClubViewModel by viewModels()

    private val rvBoard: RecyclerView by lazy { binding.recyclerView }
    private lateinit var adapter: ProfileBasicAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentClubAllBinding.inflate(inflater, container, false)
        val root: View = binding.root

        clubViewModel.getClubs(isMyClub = false)
        clubViewModel.clubs.observe(viewLifecycleOwner) { clubs ->
            Log.e("clubAllFragment", clubs.toString())
            setupAdapter(clubs)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupAdapter(clubs: List<Club>) {
        adapter =
            ProfileBasicAdapter(clubs.map { Club.toRecyclerViewBasicItem(it) })

        adapter.setItemClickListener(object : ProfileBasicAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val intent = Intent(
                    requireContext(),
                    ClubDetailActivity::class.java
                ).apply {
                    putExtra("club", clubs[position])
                }
                Log.e("clubAllFragment", "club: ${clubs[position]}")
                startActivity(intent)
            }
        })
        rvBoard.adapter = adapter
        rvBoard.layoutManager = LinearLayoutManager(requireContext())
    }
}