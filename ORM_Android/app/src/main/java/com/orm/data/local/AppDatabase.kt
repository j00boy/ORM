package com.orm.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.orm.data.model.Club

@Database(entities = [Club::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun clubDao(): ClubDao
}