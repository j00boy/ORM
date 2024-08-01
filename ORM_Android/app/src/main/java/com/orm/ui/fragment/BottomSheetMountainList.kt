package com.orm.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.orm.data.model.Mountain
import com.orm.databinding.FragmentBottomSheetMountainBinding
import com.orm.ui.adapter.ProfileBasicAdapter
import com.orm.viewmodel.MountainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetMountainList : BottomSheetDialogFragment() {
    private var _binding: FragmentBottomSheetMountainBinding? = null
    private val binding get() = _binding!!
    private val mountainViewModel: MountainViewModel by viewModels()
    private val rvBoard: RecyclerView by lazy { binding.recyclerView }
    private lateinit var adapter: ProfileBasicAdapter
    private var listener: OnMountainSelectedListener? = null

    interface OnMountainSelectedListener {
        fun onMountainSelected(mountain: Mountain)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetMountainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mountainName = arguments?.getString(ARG_MOUNTAIN_NAME) ?: ""
        Log.d("BottomSheetMountainList", "mountainName: $mountainName")

        mountainViewModel.fetchMountainByName(mountainName)
        mountainViewModel.mountains.observe(viewLifecycleOwner) {
            setupAdapter(it!!)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupAdapter(mountains: List<Mountain>) {
        adapter =
            ProfileBasicAdapter(mountains.map { Mountain.toRecyclerViewBasicItem(it) })

        adapter.setItemClickListener(object : ProfileBasicAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                listener?.onMountainSelected(mountains[position])
                dismiss()
            }
        })
        rvBoard.adapter = adapter
        rvBoard.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnMountainSelectedListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnMountainSelectedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {
        private const val ARG_MOUNTAIN_NAME = "mountain_name"

        fun newInstance(mountainName: String): BottomSheetMountainList {
            val fragment = BottomSheetMountainList()
            val args = Bundle()
            args.putString(ARG_MOUNTAIN_NAME, mountainName)
            fragment.arguments = args
            return fragment
        }
    }
}
