package com.orm.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.orm.databinding.FragmentHomeBinding
import com.orm.ui.club.ClubActivity
import com.orm.ui.mountain.MountainSearchActivity
import com.orm.ui.trace.TraceActivity
import com.orm.util.NetworkUtils

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
            if (NetworkUtils.isNetworkError(requireContext())) return@setOnClickListener
            startActivity(MountainSearchActivity::class.java)
        }
        binding.cardClub.setOnClickListener {
            if (NetworkUtils.isNetworkError(requireContext())) return@setOnClickListener
            startActivity(ClubActivity::class.java)
        }
        binding.cardTrace.setOnClickListener {
            startActivity(TraceActivity::class.java)
        }
    }

    private fun startActivity(targetActivityClass: Class<*>) {
        val intent = Intent(requireActivity(), targetActivityClass)
        startActivity(intent)
    }

    private fun setupCardFragments() {
        setupCardFragment(binding.cardSearch.id, "검색", SUB_MOUNTAIN)
        setupCardFragment(binding.cardTrace.id, "발자국", SUB_TRACE)
        setupCardFragment(binding.cardClub.id, "모임", SUB_CLUB)
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

    companion object {
        val SUB_MOUNTAIN: String = "산을\n찾으세요"
        val SUB_TRACE: String = "발자국을 추적하세요"
        val SUB_CLUB: String = "모임을\n찾으세요"
    }
}
