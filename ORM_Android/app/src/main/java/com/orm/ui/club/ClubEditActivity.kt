package com.orm.ui.club

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.orm.data.model.club.Club
import com.orm.data.model.club.ClubCreate
import com.orm.databinding.ActivityClubEditBinding
import com.orm.viewmodel.ClubViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class ClubEditActivity : AppCompatActivity() {
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
            Toast.makeText(
                this,
                "산 검색 ${binding.tfClubMountain.editText?.text.toString()}",
                Toast.LENGTH_SHORT
            ).show()
        }

        val content = if (club == null) "생성" else "수정"
        binding.btnSign.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle(content)
                .setMessage("모임을 $content 하시겠습니까?")
                .setNegativeButton("취소") { _, _ -> }
                .setPositiveButton("확인") { dialog, which ->

                    val clubCreate = ClubCreate(
                        clubName = binding.tfClubName.editText?.text.toString(),
                        description = binding.tfClubDesc.editText?.text.toString(),
                        mountainId = 1
                    )

                    // TODO : image upload
                    clubViewModel.createClubs(clubCreate, null)
                    dialog.dismiss()
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
}
