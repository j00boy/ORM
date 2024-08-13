package com.orm.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.orm.databinding.FragmentHomeInfoBinding
import com.orm.ui.mountain.MountainDetailActivity
import com.orm.viewmodel.MountainViewModel
import com.orm.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeInfoFragment : Fragment() {
    private var _binding: FragmentHomeInfoBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by viewModels()
    private val mountainViewModel: MountainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeInfoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        userViewModel.user.observe(viewLifecycleOwner) {
            binding.user = it
        }

        binding.cvRecommend.setOnClickListener {
            mountainViewModel.getMountainsRecommend()

            mountainViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                if (!isLoading) {
                    mountainViewModel.mountain.value?.let { mountain ->
                        startActivity(
                            Intent(requireContext(), MountainDetailActivity::class.java).apply {
                                putExtra("mountain", mountain)
                            }
                        )
                        mountainViewModel.isLoading.removeObservers(viewLifecycleOwner)
                    }
                }
            }
        }




        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}