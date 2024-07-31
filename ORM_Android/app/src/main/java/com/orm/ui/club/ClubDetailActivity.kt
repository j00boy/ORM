package com.orm.ui.club

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.orm.data.model.RequestMember
import com.orm.data.model.club.Club
import com.orm.data.model.club.ClubCreate
import com.orm.databinding.ActivityClubDetailBinding
import com.orm.viewmodel.ClubViewModel
import com.orm.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClubDetailActivity : AppCompatActivity() {
    private val binding: ActivityClubDetailBinding by lazy {
        ActivityClubDetailBinding.inflate(layoutInflater)
    }

    private val userViewModel: UserViewModel by viewModels()
    private val clubViewModel: ClubViewModel by viewModels()

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
        Log.d("ClubDetailActivity", "club: $club")

        binding.club = club

        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnMember.setOnClickListener {
            if (club?.isMember == true) {
                val intent = Intent(this, ClubMemberActivity::class.java)
                intent.putExtra("club", club)
                startActivity(intent)
            }
        }

        userViewModel.user.observe(this) {
            if (it != null && it.userId == club?.managerId) {
                binding.btnEdit.visibility = View.VISIBLE
            } else {
                binding.btnEdit.visibility = View.INVISIBLE
            }
        }

        binding.btnEdit.setOnClickListener {
            val intent = Intent(this, ClubEditActivity::class.java)
            intent.putExtra("club", club)
            startActivity(intent)
        }

        binding.btnSign.setOnClickListener {
            if (club?.isMember == true) {
                // TODO : 채팅 서비스
//                startActivity(Intent(this, ChatActivity::class.java))
            } else {
                val input = EditText(this).apply {
                    hint = "자기소개를 입력해주세요."
                }

                MaterialAlertDialogBuilder(this)
                    .setView(input)
                    .setTitle("가입")
                    .setMessage("모임을 가입하시겠습니까?")
                    .setNegativeButton("취소") { _, _ -> }
                    .setPositiveButton("확인") { dialog, which ->
                        clubViewModel.applyClubs(
                            RequestMember(
                                clubId = club!!.id,
                                introduction = input.text.toString(),
                                userId = userViewModel.user.value!!.userId.toInt()
                            )
                        )
                        dialog.dismiss()
                        finish()
                    }
                    .show()
            }
        }

    }
}