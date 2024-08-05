package com.orm.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import kotlin.math.cos
import kotlin.math.sqrt

@Parcelize
@Entity(tableName = "point")
data class Point(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val trailId: Int? = null,
    val x: Double,
    val y: Double,
    val altitude: Double? = null,
    val time: LocalDateTime? = null,
    val d: Int? = null,
) : Parcelable {
    companion object {
        fun getDistance(point1: Point, point2: Point): Double {
            val R = 6371e3

            val φ1 = Math.toRadians(point1.x)
            val φ2 = Math.toRadians(point2.x)
            val λ1 = Math.toRadians(point1.y)
            val λ2 = Math.toRadians(point2.y)

            val x = (λ2 - λ1) * cos((φ1 + φ2) / 2)
            val y = φ2 - φ1
            val flatDistance = R * sqrt(x * x + y * y)

            var altitudeDifference = 0.0
            if (!(point1.altitude == null || point2.altitude == null)) {
                altitudeDifference = point2.altitude - point1.altitude
            }

            return sqrt(flatDistance * flatDistance + altitudeDifference * altitudeDifference)
        }
    }
}