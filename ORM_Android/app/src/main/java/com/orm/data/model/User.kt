package com.orm.data.model

import android.os.Parcelable
import com.orm.data.model.recycler.RecyclerViewButtonItem
import kotlinx.parcelize.Parcelize

data class User(
    val userId: String,
    val imageSrc: String,
    val nickname: String,
)