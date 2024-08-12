package com.orm.ui.board

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.orm.R
import com.orm.data.model.club.Club
import com.orm.databinding.ActivityBoardBinding
import com.orm.ui.fragment.board.BoardAllFragment
import com.orm.viewmodel.BoardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BoardActivity : AppCompatActivity() {
    private val binding: ActivityBoardBinding by lazy {
        ActivityBoardBinding.inflate(layoutInflater)
    }

    private val club: Club? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("club", Club::class.java)
        } else {
            intent.getParcelableExtra<Club>("club")
        }
    }

    private val boardViewModel: BoardViewModel by viewModels()
    private lateinit var editActivityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var detailActivityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // ActivityResultLauncher 초기화
        editActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            Log.d("test123","test123 edit : ${result.resultCode}")
            if (result.resultCode == RESULT_OK) {
                refreshBoardList()
            }
        }

        detailActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            Log.d("test123","test123 board : ${result.resultCode}")
            if (result.resultCode == RESULT_OK) {
                refreshBoardList()
            }
        }

        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.edit -> {
                    val intent = Intent(this, BoardEditActivity::class.java)
                    intent.putExtra("club", club)
                    editActivityResultLauncher.launch(intent)
                    true
                }
                else -> false
            }
        }

        // BoardAllFragment로 데이터 전달
        if (savedInstanceState == null) {
            val bundle = Bundle().apply {
                club?.let {
                    putParcelable("club", it)
                }
            }
            val fragment = BoardAllFragment().apply {
                arguments = bundle
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.info, fragment)
                .commit()
        }
    }

    private fun refreshBoardList() {
        val fragment = supportFragmentManager.findFragmentById(R.id.info) as? BoardAllFragment
        fragment?.refreshData()
    }
}