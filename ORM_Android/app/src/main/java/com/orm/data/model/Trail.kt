package com.orm.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trail")
data class Trail(
    @PrimaryKey val id: Int,
    val peekLatitude: Double,
    val peekLongitude: Double,
    val startLatitude: Double,
    val startLongitude: Double,
    val trailDetails: List<Point>,
    val heuristic: Double,
    val distance: Double
)