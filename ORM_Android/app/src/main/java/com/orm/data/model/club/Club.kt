package com.orm.data.model.club

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "club")
data class Club(
    @PrimaryKey val id: Int,
    val clubName: String,
    val description: String,
    val imageSrc: String,
    val managerId: String,
    val managerName: String,
    val memberCount: String,
    val mountainId: String,
    val mountainName: String,
    val isApplied: Boolean,
    val isMember: Boolean,
)