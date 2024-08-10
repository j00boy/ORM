package com.orm.data.model.board

import android.os.Parcelable
import com.orm.data.model.board.BoardList.Companion.truncateCreatedAt
import com.orm.data.model.recycler.RecyclerViewCommentItem
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Parcelize
data class Comment(
    val commentId: Int,
    val userId: Int,
    val userNickname: String,
    val content: String,
    val createdAt: String
) : Parcelable {
    companion object {
        fun truncateCreatedAt(createdAt: String): String {
            return if (createdAt.length > 10) {
                createdAt.substring(0, 10)
            } else {
                createdAt
            }
        }

        fun toRecyclerViewCommentItem(comment: Comment): RecyclerViewCommentItem {
            return RecyclerViewCommentItem(
                commentId = comment.commentId,
                userId = comment.userId,
                userNickname = comment.userNickname,
                content = comment.content,
                createdAt = truncateCreatedAt(comment.createdAt)
            )
        }
    }
}
