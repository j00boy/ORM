package com.orm.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.orm.databinding.FragmentHomeBinding
import com.orm.ui.club.ClubActivity
import com.orm.ui.mountain.MountainSearchActivity
import com.orm.ui.trace.TraceActivity

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupCardClickListeners()
        setupCardFragments()

        return binding.root
    }

    private fun setupCardClickListeners() {
        binding.cardSearch.setOnClickListener {
            startActivity(MountainSearchActivity::class.java)
        }
        binding.cardTrace.setOnClickListener {
            startActivity(TraceActivity::class.java)
        }
        binding.cardClub.setOnClickListener {
            startActivity(ClubActivity::class.java)
        }
    }

    private fun startActivity(targetActivityClass: Class<*>) {
        val intent = Intent(requireActivity(), targetActivityClass)
        startActivity(intent)
    }

    private fun setupCardFragments() {
        setupCardFragment(binding.cardSearch.id, "검색", "산을 찾으세요")
        setupCardFragment(binding.cardTrace.id, "발자국", "발자국을\n추적하세요")
        setupCardFragment(binding.cardClub.id, "모임", "모임을\n찾으세요")
    }

    private fun setupCardFragment(containerId: Int, title: String, subtitle: String) {
        val fragment = HomeCardFragment.newInstance(title, subtitle)
        childFragmentManager.beginTransaction()
            .replace(containerId, fragment)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
