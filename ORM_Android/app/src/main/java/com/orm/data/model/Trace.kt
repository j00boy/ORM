package com.orm.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.Date

@Entity(tableName = "trace")
data class Trace(
    @PrimaryKey val id: Int,
    val title: String,
    val hikingDate: String?,
    val hikingEndedAt: String?,
    val hikingStartedAt: String?,
    val maxHeight: Double?,
    val mountainId: Int,
    val mountainName: String,
    val coordinates: List<Point>?,
    val trailId: Int
)