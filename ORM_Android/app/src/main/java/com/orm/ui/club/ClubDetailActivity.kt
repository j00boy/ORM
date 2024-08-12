package com.orm.ui.club

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.orm.data.model.RequestMember
import com.orm.data.model.club.Club
import com.orm.data.model.club.ClubCreate
import com.orm.databinding.ActivityClubDetailBinding
import com.orm.ui.PhotoViewerActivity
import com.orm.ui.mountain.MountainDetailActivity
import com.orm.ui.board.BoardActivity
import com.orm.ui.board.BoardEditActivity
import com.orm.viewmodel.ClubViewModel
import com.orm.viewmodel.MountainViewModel
import com.orm.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClubDetailActivity : AppCompatActivity() {
    private val binding: ActivityClubDetailBinding by lazy {
        ActivityClubDetailBinding.inflate(layoutInflater)
    }

    private val userViewModel: UserViewModel by viewModels()
    private val clubViewModel: ClubViewModel by viewModels()
    private val mountainViewModel: MountainViewModel by viewModels()

    private var club: Club? = null

    private val createClubLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val clubCreated = data?.getBooleanExtra("clubCreated", false) ?: false
                if (clubCreated && club != null) {
                    clubViewModel.getClubById(club!!.id)
                    clubViewModel.club.observe(this) {
                        club = it
                        binding.club = it
                    }

                    setResult(Activity.RESULT_OK, Intent().apply {
                        putExtra("clubChanged", true)
                    })
                }
            }

            if (result.resultCode == 1) {
                val data: Intent? = result.data
                val clubMember = data?.getBooleanExtra("clubMember", false) ?: false
                if (clubMember) {
                    clubViewModel.getClubById(club!!.id)
                    clubViewModel.club.observe(this) {
                        club = it
                        binding.club = it
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        club = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("club", Club::class.java)
        } else {
            intent.getParcelableExtra<Club>("club")
        }

        binding.club = club

        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.cvThumbnail.setOnClickListener {
            val intent = Intent(this, PhotoViewerActivity::class.java)
            intent.putExtra("IMAGE_URL", club?.imgSrc)
            startActivity(intent)
        }

        binding.btnMember.setOnClickListener {
            if (club?.isMember == true) {
                createClubLauncher.launch(Intent(this, ClubMemberActivity::class.java).apply {
                    putExtra("club", club)
                })
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
            createClubLauncher.launch(
                Intent(this, ClubEditActivity::class.java).apply {
                    putExtra("club", club)
                }
            )
        }

        binding.tfClubMountain.setOnClickListener {
            moveToMountainDetail()
        }

        binding.tfClubMountainField.setOnClickListener {
            moveToMountainDetail()
        }

        binding.btnSign.setOnClickListener {
            if (club?.isMember == true) {
                // TODO : 채팅 서비스
                val intent = Intent(this, BoardActivity::class.java)
                intent.putExtra("club", club)
                startActivity(intent)
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

        setResult(1, Intent().apply {
            putExtra("clubChanged", true)
        })
    }

    private fun moveToMountainDetail() {
        Log.d("clubTest", "click")
        mountainViewModel.fetchMountainById(club?.mountainId!!.toInt(), false)
        mountainViewModel.mountain.observe(this){
            val intent = Intent(
                this@ClubDetailActivity,
                MountainDetailActivity::class.java
            ).apply {
                putExtra("mountain", it)
            }
            startActivity(intent)

            mountainViewModel.mountain.removeObservers(this)
        }
    }
}