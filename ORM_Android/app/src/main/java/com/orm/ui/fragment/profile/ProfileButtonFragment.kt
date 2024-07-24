package com.orm.ui.fragment.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.orm.databinding.FragmentChatListBinding
import com.orm.databinding.FragmentProfileBinding
import com.orm.databinding.FragmentProfileButtonBinding
import com.orm.databinding.FragmentProfileNumberBinding

class ProfileButtonFragment : Fragment() {
    private var _binding: FragmentProfileButtonBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProfileButtonBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}