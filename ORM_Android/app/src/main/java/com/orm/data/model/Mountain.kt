package com.orm.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mountain")
data class Mountain(
    @PrimaryKey val id: Int,
    val name: String,
    val address: String,
    val imageSrc: String,
    val altitude: Double,
    val description: String
)