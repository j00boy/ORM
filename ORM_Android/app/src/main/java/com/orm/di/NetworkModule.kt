package com.orm.di

import com.orm.BuildConfig
import com.orm.data.api.ClubService
import com.orm.data.api.MountainService
import com.orm.data.api.TraceService
import com.orm.data.api.UserService
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
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideClubService(retrofit: Retrofit): ClubService {
        return retrofit.create(ClubService::class.java)
    }

    @Provides
    @Singleton
    fun provideMountainService(retrofit: Retrofit): MountainService {
        return retrofit.create(MountainService::class.java)

    }

    @Provides
    @Singleton
    fun provideTraceService(retrofit: Retrofit): TraceService {
        return retrofit.create(TraceService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }
}