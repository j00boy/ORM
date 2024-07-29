package com.orm.data.api

import com.orm.data.model.ApproveClub
import com.orm.data.model.Club
import com.orm.data.model.CreateClub
import com.orm.data.model.RequestMember
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Query

interface ClubService {
    @GET("clubs")
    fun getClubs(
        @Header("Authorization") accessToken: String = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhY2Nlc3MtdG9rZW4iLCJpYXQiOjE3MjE3MDk2MDYsImV4cCI6MjA4MTcwOTYwNiwidXNlcklkIjo1fQ.N8yIJ6L5n4QzKjMgC08D-d_b_kTwaRyAOjyZ1nd-YKY",
        @Query("pgno") pgno: Int = 0,
        @Query("recordSize") recordSize: Int = 10,
        @Query("keyword") keyword: String = "",
        @Query("isMyClub") isMyClub: Boolean = false,
    ): Call<List<Club>>

    @GET("clubs/members")
    fun getMembers(
        @Header("Authorization") accessToken: String,
        @Query("clubId") clubId: Int): Call<Map<String, List<Any>>>

    // TODO: implements club service

    @POST("clubs/members/approve")
    fun approveClubs(
        @Header("Authorization") accessToken: String,
        @Body approveClub: ApproveClub,
    ): Call<Unit>

    @DELETE("clubs/members/leave")
    fun leaveClubs(
        @Header("Authorization") accessToken: String,
        @Query("clubId") clubId: Int,
        @Query("userId") userId: Int,
    ): Call<Unit>

    @POST("clubs/members/apply")
    fun applyClubs(
        @Header("Authorization") accessToken: String,
        @Body requestMember: RequestMember,
    ): Call<Unit>

    @Multipart
    @POST("clubs/create")
    fun createClubs(
        @Header("Authorization") accessToken: String,
        @Part("createClub") createClub: RequestBody,
        @Part imgFile: MultipartBody.Part?
    ): Call<ResponseBody>

    @GET("clubs/name/check-duplicate")
    fun checkDuplicateClubs(
        @Header("Authorization") accessToken: String,
        @Query("name") name:String,
    ): Call<Unit>
}