package com.orm.data.api

import com.orm.data.model.Club
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ClubService {
    @GET("clubs")
    fun getClubs(
        @Query("pgno") pgno: Int = 0,
        @Query("recordSize") recordSize: Int = 10,
        @Query("keyword") keyword: String = "",
        @Query("isMyClub") isMyClub: Boolean = false,
    ): Call<List<Club>>

    @GET("clubs/members")
    fun getMembers(@Query("clubId") clubId: Int): Call<Any>

    // TODO: implements club service
//    @POST("clubs/members/approve")
//    @DELETE("clubs/members/leave")
//    @POST("clubs/members/apply")
//    @POST("clubs/create")
//    @GET("clubs/name/check-duplicate")
}