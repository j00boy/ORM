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
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(appInterceptor: AppInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(appInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()
    }

    @Provides
    @Singleton
    fun provideAppInterceptor(): AppInterceptor {
        return AppInterceptor()
    }

    class AppInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
            val newRequest = request().newBuilder()
                .addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhY2Nlc3MtdG9rZW4iLCJpYXQiOjE3MjE3MDM4NDAsImV4cCI6MjA4MTcwMzg0MCwidXNlcklkIjo1fQ.KaAVNkL9-6pxSexWPrF6BOG8gL_HlKkF_JLzF2qnTiI") // 예시 헤더
                .build()
            proceed(newRequest)
        }
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
