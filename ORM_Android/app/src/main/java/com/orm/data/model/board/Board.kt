package com.orm.data.model.board

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Parcelize
data class Board(
    val boardId: Int,
    val title: String,
    val content: String,
    val userId: Int,
    val userNickname: String,
    val commentCount: Int,
    val comments: List<Comment>,
    val hit: Int,
    val createdAt: String,
    val imgSrcs: List<BoardImgSrc>
) : Parcelable {
    companion object {
        fun truncateCreatedAt(createdAt: String): String {
            return if (createdAt.length > 10) {
                createdAt.substring(0, 10)
            } else {
                createdAt
            }
        }
    }

    fun getTruncatedCreatedAt(): String {
        return truncateCreatedAt(createdAt)
    }
}