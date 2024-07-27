package com.orm.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class Point(
    val id: Int? = null,
    val traceId: Int? = null,
    val latitude: Double,
    val longitude: Double,
    val altitude: Double? = null,
    val time: LocalDateTime? = null,
    val difficulty: Int? = null
) : Parcelable