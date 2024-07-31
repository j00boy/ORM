package com.orm.ui.club

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orm.data.model.ClubMember
import com.orm.data.model.User
import com.orm.data.model.club.Club
import com.orm.databinding.ActivityClubMemberBinding
import com.orm.ui.adapter.ProfileButtonAdapter
import com.orm.viewmodel.ClubViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClubMemberActivity : AppCompatActivity() {
    private val binding: ActivityClubMemberBinding by lazy {
        ActivityClubMemberBinding.inflate(layoutInflater)
    }
    private val clubViewModel: ClubViewModel by viewModels()
    private val rvMemberList: RecyclerView by lazy { binding.rvMemberList }
    private val rvApplicant: RecyclerView by lazy { binding.rvApplicant }
    private lateinit var adapterMemberList: ProfileButtonAdapter
    private lateinit var adapterApplicant: ProfileButtonAdapter

    private val club: Club? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("club", Club::class.java)
        } else {
            intent.getParcelableExtra<Club>("club")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        clubViewModel.getMembers(club!!.id)
        clubViewModel.members.observe(this@ClubMemberActivity){
            Log.d("clubTest", it["applicants"].toString())
            setupAdapterMemberList(it["members"])
            setupAdapterApplicant(it["applicants"])
        }

        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupAdapterMemberList(members: List<ClubMember>?) {
        adapterMemberList =
            ProfileButtonAdapter(members!!.map { ClubMember.toRecyclerViewButtonItem(it) })

        rvMemberList.adapter = adapterMemberList
        rvMemberList.layoutManager = LinearLayoutManager(this@ClubMemberActivity)
    }

    private fun setupAdapterApplicant(applicant: List<ClubMember>?) {
        adapterApplicant =
            ProfileButtonAdapter(applicant!!.map { ClubMember.toRecyclerViewButtonItem(it) })

        rvApplicant.adapter = adapterApplicant
        rvApplicant.layoutManager = LinearLayoutManager(this@ClubMemberActivity)
    }
}