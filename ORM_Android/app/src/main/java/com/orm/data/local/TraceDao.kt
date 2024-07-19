package com.orm.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.orm.data.model.Trace

@Dao
interface TraceDao {
    @Query("SELECT * FROM Trace")
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun getAllTraces(): List<Trace>

    @Query("SELECT * FROM Trace WHERE id = :id")
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun getTrace(id: Int): Trace

    @Delete
    suspend fun removeTrace(trace: Trace): Int
}