package com.orm.data.model.board

import android.os.Parcelable
import com.orm.data.model.recycler.RecyclerViewCommentItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class Comment(
    val commentId : Int,
    val userId : Int,
    val userNickname: String,
    val content : String,
    val createdAt : String
): Parcelable {
    companion object {
        fun toRecyclerViewCommentItem(comment: Comment): RecyclerViewCommentItem {
            return RecyclerViewCommentItem(
                commentId = comment.commentId,
                userId = comment.userId,
                userNickname = comment.userNickname,
                content = comment.content,
                createdAt = comment.createdAt,
            )
        }
    }
}
