package com.orm.data.local

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.orm.data.model.Club

interface ClubDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun getAllClubs(): List<Club>

    @Delete
    suspend fun removeAllClubs(): Unit
}