package com.orm.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.orm.databinding.FragmentHomeBinding
import com.orm.databinding.FragmentHomeInfoBinding
import com.orm.ui.ClubActivity
import com.orm.ui.MountainSearchActivity
import com.orm.ui.TraceActivity

class HomeInfoFragment : Fragment() {
    private var _binding: FragmentHomeInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeInfoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}