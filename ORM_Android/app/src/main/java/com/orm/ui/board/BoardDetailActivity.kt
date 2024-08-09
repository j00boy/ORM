package com.orm.ui.board

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.webkit.WebView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.orm.R
import com.orm.data.model.board.Board
import com.orm.data.model.board.CreateComment
import com.orm.databinding.ActivityBoardDetailBinding
import com.orm.ui.club.ClubEditActivity
import com.orm.ui.club.ClubMemberActivity
import com.orm.ui.fragment.board.CommentAllFragment
import com.orm.viewmodel.BoardViewModel
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.richeditor.RichEditor
import java.util.regex.Pattern

@AndroidEntryPoint
class BoardDetailActivity : AppCompatActivity() {
    private val binding by lazy { ActivityBoardDetailBinding.inflate(layoutInflater) }
    private val boardViewModel: BoardViewModel by viewModels()
    private var currentBoard: Board? = null
    private var processedContent: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val boardId = intent.getIntExtra("boardId", -1)
        val clubId = intent.getIntExtra("clubId", -1)
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
                    }
                }
                supportFragmentManager.beginTransaction()
                    .replace(R.id.info, commentFragment)
                    .commit()
            }
        })

        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnDelete.setOnClickListener {
            boardViewModel.deleteBoards(boardId)
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnEdit.setOnClickListener {
            currentBoard?.let { board ->
                val intent = Intent(this, BoardEditActivity::class.java)
                intent.putExtra("clubId", clubId)
                intent.putExtra("title", board.title)
                intent.putExtra("content", processedContent)
                intent.putExtra("boardId", board.boardId)
                startActivity(intent)
            }
        }

        binding.btnSubmit.setOnClickListener {
            if (binding.tfComment.text.toString().isNotEmpty()) {
                currentBoard?.let { board ->
                    boardViewModel.createComments(board.boardId, binding.tfComment.text.toString())
                    val intent = Intent(this, BoardDetailActivity::class.java)
                    intent.putExtra("clubId", clubId)
                    intent.putExtra("boardId", board.boardId)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun displayContent(content: String, board: Board) {
        val webView = findViewById<WebView>(R.id.webView)

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
}