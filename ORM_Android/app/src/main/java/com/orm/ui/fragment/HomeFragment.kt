package com.orm.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.FragmentManager
import com.orm.R
import com.orm.databinding.FragmentHomeBinding
import com.orm.ui.ClubActivity
import com.orm.ui.MountainSearchActivity
import com.orm.ui.TraceActivity

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val buttonMountainSearch : ImageButton = binding.imgSearch
        buttonMountainSearch.setOnClickListener {
            val targetActivityClass = MountainSearchActivity::class.java
            val intent = Intent(requireActivity(), targetActivityClass)
            startActivity(intent)
        }

        val buttonTrace : ImageButton = binding.imgTrace
        buttonTrace.setOnClickListener {
            val targetActivityClass = TraceActivity::class.java
            val intent = Intent(requireActivity(), targetActivityClass)
            startActivity(intent)
        }

        val buttonClub : ImageButton = binding.imgClub
        buttonClub.setOnClickListener {
            val targetActivityClass = ClubActivity::class.java
            val intent = Intent(requireActivity(), targetActivityClass)
            startActivity(intent)
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}