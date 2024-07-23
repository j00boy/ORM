package com.orm.data.api

import android.media.session.MediaSession.Token
import com.orm.data.model.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface UserService {
    @GET("users/login/kakao/auth")
    fun loginKakao(@Query("code") code: String): Call<User>

    @GET("users/login/auto")
    fun loginAuto(): Call<User>
}