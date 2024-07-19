package com.orm.di

import com.orm.data.api.ClubService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideApiService(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://70.12.247.148:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideClubService(retrofit: Retrofit) : ClubService {
        return retrofit.create(ClubService::class.java)
    }
}