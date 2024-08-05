package com.orm.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

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
) : Parcelable