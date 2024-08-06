package com.orm.ui.club

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.orm.data.model.ClubMember
import com.orm.data.model.club.Club
import com.orm.data.model.club.ClubApprove
import com.orm.databinding.ActivityClubMemberBinding
import com.orm.ui.adapter.ProfileButtonAdapter
import com.orm.viewmodel.ClubViewModel
import com.orm.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

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

    private val clubViewModel: ClubViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    private val rvMemberList: RecyclerView by lazy { binding.rvMemberList }
    private val rvApplicant: RecyclerView by lazy { binding.rvApplicant }

    private lateinit var adapterMemberList: ProfileButtonAdapter
    private lateinit var adapterApplicant: ProfileButtonAdapter

    private var userId: String? = null
    private var membersMap: Map<String, List<ClubMember>?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        userViewModel.user.observe(this) { user ->
            userId = user?.userId
            if (user != null && user.userId != club!!.managerId) {
                binding.cvApplicant.visibility = View.GONE
                binding.rvApplicant.visibility = View.GONE
            }
            checkIfDataReady()
        }

        clubViewModel.getMembers(club!!.id)
        clubViewModel.members.observe(this@ClubMemberActivity) { membersMap ->
            this.membersMap = membersMap
            Log.d("ClubMemberActivity", membersMap["members"].toString())
            Log.d("ClubMemberActivity", membersMap["applicants"].toString())
            checkIfDataReady()
        }
    }

    private fun checkIfDataReady() {
        if (userId != null && membersMap != null) {
            setupAdapterMemberList(membersMap!!["members"])
            setupAdapterApplicant(membersMap!!["applicants"])
        }
    }

    private fun setupAdapterMemberList(members: List<ClubMember>?) {
        val clubMembers = members?.map { ClubMember.toRecyclerViewButtonItem(it) } ?: emptyList()

        adapterMemberList = ProfileButtonAdapter(clubMembers)

        adapterMemberList.setType("member")
        adapterMemberList.setUserId(userId.toString())
        adapterMemberList.setManagerId(club!!.managerId)
        adapterMemberList.setItemClickListener(object : ProfileButtonAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                return
            }

            override fun onClickBtnUp(v: View, position: Int) {
                MaterialAlertDialogBuilder(this@ClubMemberActivity)
                    .setTitle("클럽 탈퇴")
                    .setMessage("정말로 ${club?.clubName} 클럽에서 탈퇴하시겠습니까?")
                    .setNegativeButton("취소") { _, _ -> }
                    .setPositiveButton("확인") { dialog, which ->
                        clubViewModel.leaveClubs(club!!.id, clubMembers[position].id!!.toInt())
                        finish()
                    }.show()
            }

            override fun onClickBtnDown(v: View, position: Int) {
                MaterialAlertDialogBuilder(this@ClubMemberActivity)
                    .setTitle("회원 추방")
                    .setMessage("정말로 ${members!![position].nickname}님을 추방하시겠습니까?")
                    .setNegativeButton("취소") { _, _ -> }
                    .setPositiveButton("확인") { dialog, which ->
                        clubViewModel.dropMember(club!!.id, clubMembers[position].id!!.toInt())
                    }.show()
            }
        })

        rvMemberList.adapter = adapterMemberList
        rvMemberList.layoutManager = LinearLayoutManager(this@ClubMemberActivity)
    }

    private fun setupAdapterApplicant(applicant: List<ClubMember>?) {
        val applicants = applicant?.map { ClubMember.toRecyclerViewButtonItem(it) } ?: emptyList()

        adapterApplicant = ProfileButtonAdapter(applicants)

        adapterApplicant.setType("applicant")

        adapterApplicant.setItemClickListener(object : ProfileButtonAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                return
            }

            override fun onClickBtnUp(v: View, position: Int) {
                MaterialAlertDialogBuilder(this@ClubMemberActivity)
                    .setTitle("가입 수락")
                    .setMessage("가입을 수락하시겠습니까?")
                    .setNegativeButton("취소") { _, _ -> }
                    .setPositiveButton("확인") { dialog, which ->
                        clubViewModel.approveClubs(
                            ClubApprove(
                                club!!.id,
                                applicants[position].id!!,
                                true
                            )
                        )
                        dialog.dismiss()
                        finish()
                    }
                    .show()
            }

            override fun onClickBtnDown(v: View, position: Int) {
                MaterialAlertDialogBuilder(this@ClubMemberActivity)
                    .setTitle("가입 거절")
                    .setMessage("가입을 거절하시겠습니까?")
                    .setNegativeButton("취소") { _, _ -> }
                    .setPositiveButton("확인") { dialog, which ->
                        clubViewModel.approveClubs(
                            ClubApprove(
                                club!!.id,
                                applicants[position].id!!,
                                false
                            )
                        )
                        dialog.dismiss()
                        finish()
                    }
                    .show()
            }
        })

        rvApplicant.adapter = adapterApplicant
        rvApplicant.layoutManager = LinearLayoutManager(this@ClubMemberActivity)
    }
}
