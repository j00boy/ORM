package com.orm.ui.club

import com.orm.ui.fragment.BottomSheetMountainList
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isNotEmpty
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.orm.data.model.Mountain
import com.orm.data.model.club.Club
import com.orm.data.model.club.ClubCreate
import com.orm.databinding.ActivityClubEditBinding
import com.orm.viewmodel.ClubViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClubEditActivity : AppCompatActivity(), BottomSheetMountainList.OnMountainSelectedListener {
    private val binding: ActivityClubEditBinding by lazy {
        ActivityClubEditBinding.inflate(layoutInflater)
    }

    private val club: Club? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("club", Club::class.java)
        } else {
            intent.getParcelableExtra<Club>("club")
        }
    }

    private var mountainId: Int = 0
    private val clubViewModel: ClubViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.club = club

        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.tfClubName.setEndIconOnClickListener {
            clubViewModel.isOperationSuccessful.removeObservers(this)

            clubViewModel.resetOperationStatus()

            clubViewModel.isOperationSuccessful.observe(this) { isDuplicate ->
                isDuplicate?.let {
                    Log.d("ClubEditActivity", "isDuplicate: $isDuplicate")
                    val message = if (isDuplicate) "생성 불가능한 이름입니다." else "생성 가능한 이름입니다."
                    showDuplicateCheckDialog(message)
                }
            }

            clubViewModel.checkDuplicateClubs(binding.tfClubName.editText?.text.toString())
        }

        binding.tfClubMountain.setEndIconOnClickListener {
            Log.d("ClubEditActivity", binding.tfClubMountain.editText?.text.toString())

            val mountainName = binding.tfClubMountain.editText?.text.toString()
            val bottomSheetFragment =
                BottomSheetMountainList.newInstance(mountainName)
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }

        val content = if (club == null) "생성" else "수정"
        binding.btnSign.setOnClickListener {
            if(binding.tfClubName.editText!!.text.isEmpty() || mountainId == 0) {
                MaterialAlertDialogBuilder(this)
                    .setTitle(content)
                    .setMessage("필수 정보를 입력해주세요.")
                    .setPositiveButton("확인"){ _,_ ->
                    }
                    .show()
                return@setOnClickListener
            }
            MaterialAlertDialogBuilder(this)
                .setTitle(content)
                .setMessage("모임을 $content 하시겠습니까?")
                .setNegativeButton("취소") { _, _ -> }
                .setPositiveButton("확인") { dialog, which ->

                    val clubCreate = ClubCreate(
                        clubName = binding.tfClubName.editText?.text.toString(),
                        description = binding.tfClubDesc.editText?.text.toString(),
                        mountainId = mountainId
                    )

                    // TODO : image upload
                    if (club == null) {
                        Log.d("ClubEditActivity create", "club: $club")
                        clubViewModel.createClubs(clubCreate, null)
                    } else {
                        Log.d("ClubEditActivity edit", "club: $club")
                        clubViewModel.updateClubs(club!!.id, clubCreate, null)
                    }
                    dialog.dismiss()
                    finish()
                }
                .show()
        }
    }

    private fun showDuplicateCheckDialog(message: String) {
        MaterialAlertDialogBuilder(this)
            .setTitle("중복 체크")
            .setMessage(message)
            .setPositiveButton("확인") { dialog, which -> }
            .show()
    }

    override fun onMountainSelected(mountain: Mountain) {
        binding.tfClubMountain.editText?.setText(mountain.name)
        mountainId = mountain.id
    }
}
