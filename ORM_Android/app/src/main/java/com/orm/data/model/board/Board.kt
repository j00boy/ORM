package com.orm.data.model.board

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Board(
    val boardId : Int,
    val title : String,
    val content : String,
    val userId : Int,
    val userNickname : String,
    val commentCount : Int,
    val comments : List<Comment>,
    val hit : Int,
    val createdAt : String,
    val imgSrcs : List<BoardImgSrc>
): Parcelable
