package com.orm.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mountain")
data class Mountain(
    @PrimaryKey val id: Int,
    val name: String,
    val address: String,
    val code: Int,
    val imageSrc: String,
    val desc: String,
    val height: Double,
    val trails: List<Trail>
)