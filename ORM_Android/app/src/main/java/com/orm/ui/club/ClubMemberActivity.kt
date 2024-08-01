package com.orm.ui.club

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orm.data.local.PreferencesKeys
import com.orm.data.model.ClubMember
import com.orm.data.model.User
import com.orm.data.model.club.Club
import com.orm.data.model.club.ClubApprove
import com.orm.databinding.ActivityClubMemberBinding
import com.orm.ui.adapter.ProfileButtonAdapter
import com.orm.util.dataStore
import com.orm.viewmodel.ClubViewModel
import com.orm.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

@AndroidEntryPoint
class ClubMemberActivity : AppCompatActivity() {
    private val binding: ActivityClubMemberBinding by lazy {
        ActivityClubMemberBinding.inflate(layoutInflater)
    }

    private val club: Club? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("club", Club::class.java)
        } else {
            intent.getParcelableExtra<Club>("club")
        }
    }

    private lateinit var userId: String

    private val clubViewModel: ClubViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    private val rvMemberList: RecyclerView by lazy { binding.rvMemberList }
    private val rvApplicant: RecyclerView by lazy { binding.rvApplicant }

    private lateinit var adapterMemberList: ProfileButtonAdapter
    private lateinit var adapterApplicant: ProfileButtonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        userViewModel.user.observe(this) {
            if (it != null && it.userId != club!!.managerId) {
                binding.cvApplicant.visibility = View.GONE
                binding.rvApplicant.visibility = View.GONE
            }
        }

        clubViewModel.getMembers(club!!.id)
        clubViewModel.members.observe(this@ClubMemberActivity) {
            Log.d("ClubMemberActivity", it["members"].toString())
            Log.d("ClubMemberActivity", it["applicants"].toString())
            setupAdapterMemberList(it["members"])
            setupAdapterApplicant(it["applicants"])
        }
    }

    private fun setupAdapterMemberList(members: List<ClubMember>?) {
        val clubMembers = members!!.map { ClubMember.toRecyclerViewButtonItem(it) }

        adapterMemberList = ProfileButtonAdapter(clubMembers)

        adapterMemberList.setType("member")
//        adapterMemberList.setUserId(userId)

        adapterMemberList.setItemClickListener(object : ProfileButtonAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                TODO("Not yet implemented")
            }

            override fun onClickBtnUp(v: View, position: Int) {
                clubViewModel.leaveClubs(club!!.id, userId.toInt())
            }

            override fun onClickBtnDown(v: View, position: Int) {
                TODO("Not yet implemented")
            }

        })

        rvMemberList.adapter = adapterMemberList
        rvMemberList.layoutManager = LinearLayoutManager(this@ClubMemberActivity)
    }

    private fun setupAdapterApplicant(applicant: List<ClubMember>?) {
        val applicants = applicant!!.map { ClubMember.toRecyclerViewButtonItem(it) }

        adapterApplicant = ProfileButtonAdapter(applicants)

        adapterApplicant.setType("applicant")

        adapterApplicant.setItemClickListener(object : ProfileButtonAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                TODO("Not yet implemented")
            }

            override fun onClickBtnUp(v: View, position: Int) {
                clubViewModel.approveClubs(ClubApprove(club!!.id, applicants[position].id!!, true))
            }

            override fun onClickBtnDown(v: View, position: Int) {
                clubViewModel.approveClubs(ClubApprove(club!!.id, applicants[position].id!!, false))
            }
        })

        rvApplicant.adapter = adapterApplicant
        rvApplicant.layoutManager = LinearLayoutManager(this@ClubMemberActivity)
    }
}