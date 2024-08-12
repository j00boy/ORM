package com.orm.ui.fragment.board

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.orm.R
import com.orm.data.model.board.Board
import com.orm.data.model.board.BoardList
import com.orm.data.model.board.Comment
import com.orm.data.model.club.Club
import com.orm.data.model.recycler.RecyclerViewCommentItem
import com.orm.databinding.FragmentCommentAllBinding
import com.orm.ui.adapter.ProfileCommentAdapter
import com.orm.ui.board.BoardDetailActivity
import com.orm.viewmodel.BoardViewModel
import com.orm.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommentAllFragment : Fragment() {
    private var _binding: FragmentCommentAllBinding? = null
    private val binding get() = _binding!!

    private val boardViewModel: BoardViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    private lateinit var adapter: ProfileCommentAdapter

    private val boardId: Int? by lazy {
        arguments?.getInt("boardId")
    }

    private val clubId: Int? by lazy {
        arguments?.getInt("clubId")
    }

    private val board: Board? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("board", Board::class.java)
        } else {
            arguments?.getParcelable<Board>("board")
        }
    }

    private val userId: String? by lazy {
        arguments?.getString("userId")
    }

    private val boardList: BoardList? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("boardList", BoardList::class.java)
        } else {
            arguments?.getParcelable<BoardList>("boardList")
        }
    }

    private val club: Club? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("club", Club::class.java)
        } else {
            arguments?.getParcelable<Club>("club")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCommentAllBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        board?.let {
            setupAdapter(it.comments, userId)
        }

        boardViewModel.comment.observe(viewLifecycleOwner) { updatedComment ->
            updatedComment?.let {
                val updatedList = adapter.getItems().toMutableList() // 변경된 부분
                val index = updatedList.indexOfFirst { it.commentId == updatedComment.commentId }
                if (index != -1) {
                    updatedList[index] = Comment.toRecyclerViewCommentItem(updatedComment)
                    adapter.submitList(updatedList)
                }
            }
        }

        // 삭제나 수정이 성공했는지 확인하여 UI 갱신
        boardViewModel.isOperationSuccessful.observe(viewLifecycleOwner) { success ->
            Log.d("success123" , "success123 : $success")
            if (success == true) {
                boardId?.let { boardViewModel.getBoards(it) }
            }
        }



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupAdapter(comments: List<Comment>, currentUserId: String?) {
        adapter = ProfileCommentAdapter(comments.map { Comment.toRecyclerViewCommentItem(it) },
            onEditClick = { comment ->
                if (comment.userId.toString() == currentUserId) {
                    showEditBottomSheet(comment)
                }
            },
            onDeleteClick = { comment ->
                if (comment.userId.toString() == currentUserId) {
                    boardId?.let { boardId ->
                        // 댓글 삭제 요청
                        boardViewModel.deleteComments(boardId, comment.commentId)

                        // 삭제된 댓글을 어댑터에서 제거
                        val updatedList = adapter.getItems().toMutableList()
                        updatedList.removeAll { it.commentId == comment.commentId }
                        adapter.submitList(updatedList)
                    }
                }
            }
        )

        binding.recyclerView.adapter = adapter
    }

    private fun showEditBottomSheet(comment: RecyclerViewCommentItem) {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_edit_comment, null)
        bottomSheetDialog.setContentView(bottomSheetView)

        val editTextComment = bottomSheetView.findViewById<EditText>(R.id.tf_editComment)
        val buttonUpdate = bottomSheetView.findViewById<Button>(R.id.btn_update)

        editTextComment.setText(comment.content)

        buttonUpdate.setOnClickListener {
            val updatedContent = editTextComment.text.toString()
            boardId?.let {
                boardViewModel.updateComments(it, comment.commentId, updatedContent)
            }
            Log.d("CommentAllFragment", "Edit clicked for comment: ${comment.commentId}")
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    fun addNewComment(newComment: Comment) {
        val newItem = Comment.toRecyclerViewCommentItem(newComment)
        val currentList = adapter.getItems().toMutableList()
        currentList.add(newItem)
        adapter.submitList(currentList)
        binding.recyclerView.scrollToPosition(currentList.size - 1) // 새 댓글로 스크롤
    }


}
