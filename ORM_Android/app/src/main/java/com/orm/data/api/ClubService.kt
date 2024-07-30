package com.orm.data.api

import com.orm.data.model.RequestMember
import com.orm.data.model.club.Club
import com.orm.data.model.club.ClubApprove
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ClubService {
    @GET("clubs")
    fun getClubs(
        @Query("pgno") pgno: Int = 0,
        @Query("recordSize") recordSize: Int = 1000,
        @Query("keyword") keyword: String = "",
        @Query("isMyClub") isMyClub: Boolean = false,
    ): Call<List<Club>>

    @GET("clubs/members")
    fun getMembers(
        @Query("clubId") clubId: Int
    ): Call<Map<String, List<Any>>>


    @POST("clubs/members/approve")
    fun approveClubs(
        @Body approveClub: ClubApprove,
    ): Call<Unit>

    @DELETE("clubs/members/leave")
    fun leaveClubs(
        @Query("clubId") clubId: Int,
        @Query("userId") userId: Int,
    ): Call<Unit>

    @POST("clubs/members/apply")
    fun applyClubs(
        @Body requestMember: RequestMember,
    ): Call<Unit>

    @Multipart
    @POST("clubs/create")
    fun createClubs(
        @Part("createClub") createClub: RequestBody,
        @Part imgFile: MultipartBody.Part?
    ): Call<ResponseBody>

    @GET("clubs/name/check-duplicate")
    fun checkDuplicateClubs(
        @Query("name") name: String,
    ): Call<Boolean>
}