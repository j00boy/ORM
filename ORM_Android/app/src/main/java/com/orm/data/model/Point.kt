package com.orm.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

data class Point(
    val id: Int? = null,
    val traceId: Int? = null,
    val latitude: Double,
    val longitude: Double,
    val altitude: Double? = null,
    val time: LocalDateTime? = null,
    val difficulty: Int? = null
)