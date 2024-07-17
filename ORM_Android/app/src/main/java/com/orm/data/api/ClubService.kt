package com.orm.data.api

import com.orm.data.model.Club
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ClubService {
    @GET("clubs")
    fun getClubs(): Call<List<Club>>
}