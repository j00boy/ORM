package com.orm.data.model.board

import android.os.Parcelable
import com.orm.data.model.recycler.RecyclerViewBoardItem
import kotlinx.parcelize.Parcelize

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
        fun toRecyclerViewBoardItem(boardList: BoardList): RecyclerViewBoardItem {
            return RecyclerViewBoardItem(
                id = boardList.boardId,
                title = boardList.title,
                userNickname = boardList.userNickname,
                commentCount = boardList.commentCount,
                hit = boardList.hit,
                createdAt = boardList.createdAt
            )
        }
    }
}