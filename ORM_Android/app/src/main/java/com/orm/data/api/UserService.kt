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
    @Headers("Authorization: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhY2Nlc3MtdG9rZW4iLCJpYXQiOjE3MjE3MDM4NDAsImV4cCI6MjA4MTcwMzg0MCwidXNlcklkIjo1fQ.KaAVNkL9-6pxSexWPrF6BOG8gL_HlKkF_JLzF2qnTiI")
    fun loginAuto(): Call<User>
}