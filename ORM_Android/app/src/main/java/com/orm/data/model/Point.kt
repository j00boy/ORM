package com.orm.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "point")
data class Point(
    @PrimaryKey val id: Int,
    val latitude: Double,
    val longitude: Double,
    val altitude: Double,
    val time: LocalDateTime
)