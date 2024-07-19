package com.orm.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

data class Point(
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val altitude: Double,
    val time: LocalDateTime
)