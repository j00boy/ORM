package com.orm.di

import android.content.Context
import androidx.room.Room
import com.orm.data.local.AppDatabase
import com.orm.data.local.dao.ClubDao
import com.orm.data.local.dao.MemberDao
import com.orm.data.local.dao.TraceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    fun provideClubDao(database: AppDatabase): ClubDao {
        return database.clubDao()
    }

    @Provides
    fun provideMemberDao(database: AppDatabase): MemberDao {
        return database.memberDao()
    }

    @Provides
    fun provideTraceDao(database: AppDatabase): TraceDao {
        return database.traceDao()
    }

}