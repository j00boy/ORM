package com.orm.ui.board

import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.orm.R
import com.orm.data.model.board.BoardCreate
import com.orm.data.model.club.Club
import com.orm.databinding.ActivityBoardEditBinding
import com.orm.viewmodel.BoardViewModel
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.richeditor.RichEditor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@AndroidEntryPoint
class BoardEditActivity : AppCompatActivity() {
    private val binding: ActivityBoardEditBinding by lazy {
        ActivityBoardEditBinding.inflate(layoutInflater)
    }

    private lateinit var editor: RichEditor
    private lateinit var titleEditText: EditText
    private lateinit var topAppBar: MaterialToolbar
    private val boardViewModel: BoardViewModel by viewModels()

    private var boardId: Int? = null
    private var clubId: Int? = null

    private val club: Club? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("club", Club::class.java)
        } else {
            intent.getParcelableExtra<Club>("club")
        }
    }

    private val openImagePickerLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                insertImageToEditor(it)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
        setupEditor()
        setupFormattingToolbar()

        boardId = intent.getIntExtra("boardId", -1).takeIf { it != -1 }
        clubId = intent.getIntExtra("clubId", -1).takeIf { it != -1 } ?: club?.id

        if (boardId != null) {
            val title = intent.getStringExtra("title") ?: ""
            val content = intent.getStringExtra("content") ?: ""

            titleEditText.setText(title)
            editor.html = content

            binding.topAppBar.title = "게시글 수정"
        } else {
            binding.topAppBar.title = "새 게시글 작성"
        }

        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        findViewById<MaterialButton>(R.id.btn_sign).setOnClickListener {
            savePost()
        }
    }

    private fun initViews() {
        editor = findViewById(R.id.editor)
        titleEditText = findViewById(R.id.et_title)
        topAppBar = findViewById(R.id.topAppBar)
    }

    private fun setupEditor() {
        editor.setEditorHeight(200)
        editor.setPlaceholder("내용을 입력하세요...")
        editor.setEditorFontSize(22)
        editor.setEditorFontColor(Color.BLACK)
        editor.setPadding(10, 10, 10, 10)
        editor.setInputEnabled(true)
        editor.setPlaceholder("내용을 입력하세요...")
    }

    private fun setupFormattingToolbar() {
        findViewById<ImageButton>(R.id.action_bold).setOnClickListener { editor.setBold() }
        findViewById<ImageButton>(R.id.action_italic).setOnClickListener { editor.setItalic() }
        findViewById<ImageButton>(R.id.action_underline).setOnClickListener { editor.setUnderline() }
        findViewById<ImageButton>(R.id.action_align_left).setOnClickListener { editor.setAlignLeft() }
        findViewById<ImageButton>(R.id.action_align_center).setOnClickListener { editor.setAlignCenter() }
        findViewById<ImageButton>(R.id.action_align_right).setOnClickListener { editor.setAlignRight() }

        // Image Button
        findViewById<ImageButton>(R.id.action_image).setOnClickListener {
            openImagePickerLauncher.launch("image/*")
        }
    }

    private fun insertImageToEditor(imageUri: Uri) {
        val imageUrl = imageUri.toString()

        // 이미지 크기를 조정하는 HTML 태그를 생성합니다.
        val imageHtml = "<img src=\"$imageUrl\" style=\"max-width:80%; height:auto;\" />"

        // 에디터의 현재 HTML 내용을 확인하고, null인 경우 빈 문자열로 처리합니다.
        val currentHtml = editor.html ?: ""

        // 이미지를 에디터에 삽입합니다.
        editor.html = currentHtml + imageHtml
    }

    private fun savePost() {
        val title = titleEditText.text.toString()

        Log.d("BoardEditActivity Content", "Content1 : ${editor.html}")
        val content = editor.html
        if (title.isBlank() || content.isBlank()) {
            Toast.makeText(this, "제목과 내용을 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        if (clubId == null) {
            Toast.makeText(this, "클럽 정보를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        if (boardId != null) {
            // 기존 게시글 수정
            boardViewModel.updateBoards(clubId!!, title, content, boardId!!)
            Log.d("update", "update : $boardId")
            Toast.makeText(this, "게시글이 수정되었습니다.", Toast.LENGTH_SHORT).show()
        } else {
            // 새 게시글 작성
            boardViewModel.createBoards(clubId!!, title, content)
            Toast.makeText(this, "새 게시글이 작성되었습니다.", Toast.LENGTH_SHORT).show()
        }

        setResult(RESULT_OK)
        finish()
    }
}
