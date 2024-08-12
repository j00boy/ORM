package com.orm.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.orm.data.model.Record

@Dao
interface RecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: Record) : Long

    @Delete
    suspend fun deleteRecord(record: Record): Int

    @Query("SELECT * FROM record WHERE id = :id")
    suspend fun getRecord(id: Long): Record

    @Query("DELETE FROM record")
    suspend fun deleteAllRecords()

}