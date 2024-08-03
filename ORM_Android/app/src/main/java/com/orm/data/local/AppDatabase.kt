package com.orm.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.orm.data.local.dao.ClubDao
import com.orm.data.local.dao.MemberDao
import com.orm.data.local.dao.NotificationDao
import com.orm.data.local.dao.PointDao
import com.orm.data.local.dao.RecordDao
import com.orm.data.local.dao.TraceDao
import com.orm.data.local.dao.TrailDao
import com.orm.data.model.club.Club
import com.orm.data.model.Member
import com.orm.data.model.Notification
import com.orm.data.model.Point
import com.orm.data.model.Record
import com.orm.data.model.Trace
import com.orm.data.model.Trail

@Database(entities = [Club::class, Trace::class, Member::class, Notification::class, Point::class, Trail::class, Record::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun clubDao(): ClubDao
    abstract fun memberDao(): MemberDao
    abstract fun traceDao(): TraceDao
    abstract fun notificationDao(): NotificationDao
    abstract fun pointDao(): PointDao
    abstract fun trailDao(): TrailDao
    abstract fun recordDao(): RecordDao
}