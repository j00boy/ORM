package com.orm.data.model

import android.os.Parcelable
import com.orm.data.model.recycler.RecyclerViewButtonItem
import kotlinx.parcelize.Parcelize

data class User(
    val userId: String,
    val imageSrc: String,
    val nickname: String,
    var gender: String? = "male",
    var age: Int? = 23,
    var level: Int? = 2,
)