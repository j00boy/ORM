package com.orm.data.model.board

import android.os.Parcelable
import com.orm.data.model.recycler.RecyclerViewBoardItem
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Parcelize
data class BoardList(
    val boardId : Int,
    val title : String,
    val userId : Int,
    val userNickname : String,
    val commentCount : Int,
    val hit : Int,
    val createdAt : String
): Parcelable {
    companion object {

        fun truncateCreatedAt(createdAt: String): String {
            return if (createdAt.length > 10) {
                createdAt.substring(0, 10)
            } else {
                createdAt
            }
        }

        fun toRecyclerViewBoardItem(boardList: BoardList): RecyclerViewBoardItem {
            return RecyclerViewBoardItem(
                id = boardList.boardId,
                title = boardList.title,
                userNickname = boardList.userNickname,
                commentCount = boardList.commentCount,
                hit = boardList.hit,
                createdAt = truncateCreatedAt(boardList.createdAt)
            )
        }
    }
}