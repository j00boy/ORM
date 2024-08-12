package com.orm.ui.board

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.orm.R
import com.orm.data.model.board.Board
import com.orm.data.model.board.BoardList
import com.orm.data.model.club.Club
import com.orm.databinding.ActivityBoardDetailBinding
import com.orm.ui.fragment.board.CommentAllFragment
import com.orm.viewmodel.BoardViewModel
import com.orm.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern

@AndroidEntryPoint
class BoardDetailActivity : AppCompatActivity() {
    private val binding: ActivityBoardDetailBinding by lazy {
        ActivityBoardDetailBinding.inflate(layoutInflater)
    }
    private val boardViewModel: BoardViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    private var isContentModified = false
    private var currentBoard: Board? = null
    private var processedContent: String = ""
    private lateinit var editActivityResultLauncher: ActivityResultLauncher<Intent>

    private val club: Club? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("club", Club::class.java)
        } else {
            intent.getParcelableExtra<Club>("club")
        }
    }

    private val boardList: BoardList? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("boardList", BoardList::class.java)
        } else {
            intent.getParcelableExtra<BoardList>("boardList")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val boardId = boardList?.boardId ?: -1
        val clubId = club?.id ?: -1

        // Initialize ActivityResultLauncher for editing
        editActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val refresh = result.data?.getBooleanExtra("refresh", false) ?: false
                if (refresh) {
                    isContentModified = true
                    refreshData()
                }
            }
        }

        // Observe user info
        userViewModel.user.observe(this, Observer { user ->
            val userId = user?.userId
            checkPermissions(userId)

            // Fetch board details
            boardViewModel.getBoards(boardId)

            boardViewModel.board.observe(this, Observer { board ->
                if (board != null) {
                    currentBoard = board
                    binding.board = board
                    displayContent(board.content, board)

                    val commentFragment = CommentAllFragment().apply {
                        arguments = Bundle().apply {
                            putParcelable("board", board)
                            putInt("boardId", boardId)
                            putInt("clubId", clubId)
                            putParcelable("boardList", boardList)
                            putParcelable("club", club)
                            userId?.let { putString("userId", it) } // Pass userId as a string
                        }
                    }
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.info, commentFragment)
                        .commit()
                }
            })
        })

        binding.topAppBar.setNavigationOnClickListener {
            if (isContentModified) {
                setResult(RESULT_OK, Intent().putExtra("refresh", true))
            }
            finish()
        }

        binding.btnDelete.setOnClickListener {
            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("게시글을 삭제 중입니다...")
            progressDialog.setCancelable(false)
            progressDialog.show()
            boardId.let {
                boardViewModel.deleteBoards(it)
                progressDialog.dismiss()
            }
        }

        boardViewModel.isOperationSuccessful.observe(this, Observer { isSuccess ->
            if (isSuccess == true) {
                // 삭제가 성공적으로 완료된 후에 setResult와 finish를 호출
                setResult(RESULT_OK, Intent().putExtra("refresh", true))
                finish()
            }
        })



        binding.btnEdit.setOnClickListener {
            currentBoard?.let { board ->
                val intent = Intent(this, BoardEditActivity::class.java).apply {
                    putExtra("clubId", clubId)
                    putExtra("title", board.title)
                    putExtra("content", processedContent)
                    putExtra("boardId", board.boardId)
                }
                editActivityResultLauncher.launch(intent)
            }
        }

        binding.btnSubmit.setOnClickListener {
            if (binding.tfComment.text.toString().isNotEmpty()) {
                currentBoard?.let { board ->
                    boardViewModel.createComments(board.boardId, binding.tfComment.text.toString())
                    binding.tfComment.text?.clear()
                }
            }
        }

        boardViewModel.comment.observe(this, Observer { newComment ->
            newComment?.let {
                // 새 댓글을 CommentAllFragment에 추가
                val commentFragment = supportFragmentManager.findFragmentById(R.id.info) as? CommentAllFragment
                commentFragment?.addNewComment(it)
            }
        })

    }

    private fun displayContent(content: String, board: Board) {
        val webView = findViewById<WebView>(R.id.webView)
        Log.d("detail", "detail22 :CONTENT $content")
        val pattern = Pattern.compile("<img src=\"(.*?)\"")
        val matcher = pattern.matcher(content)

        var result = content
        while (matcher.find()) {
            val contentUrl = matcher.group(1) // content:// 경로

            val fileName = contentUrl.substringAfterLast("/")
            val imgSrc = board.imgSrcs.find { it.imgSrc.contains(fileName) }?.imgSrc

            if (imgSrc != null) {
                result = result.replace(contentUrl, imgSrc, ignoreCase = false)
                Log.d("detail", "detail22 :contentUrl $contentUrl")
                Log.d("detail", "detail22 :imgSrc $imgSrc")
            }
        }
        Log.d("detail", "detail22 :result $result")
        processedContent = result
        webView.loadData(result, "text/html", "UTF-8")
    }

    private fun checkPermissions(userId: String?) {
        val boardUserId = boardList?.userId
        val clubManagerId = club?.managerId

        if (userId == boardUserId.toString() || userId == clubManagerId) {
            binding.btnDelete.visibility = View.VISIBLE
            binding.btnEdit.visibility = View.VISIBLE
        } else {
            binding.btnDelete.visibility = View.GONE
            binding.btnEdit.visibility = View.GONE
        }
    }

    private fun refreshData() {
        val boardId = boardList?.boardId ?: -1
        boardViewModel.getBoards(boardId)
    }
}
