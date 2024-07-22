package com.orm.data.api

import com.orm.data.model.Trace
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface TraceService {
    // 발자국 생성
    @POST("trace/create")
    fun createTrace(
        @Header("Authorization") accessToken: String,
        @Body trace: Trace
    ): Call<Unit>

    // 발자국 수정 (측정 전)
    @PATCH("trace/update")
    fun updateTrace(
        @Header("Authorization") accessToken: String,
        @Body trace: Trace
    ): Call<Unit>

    // 발자국 수정 (측정 후 - 이미지)
    @Multipart
    @PATCH("trace/update/images/{traceId}")
    fun updateImages(
        @Header("Authorization") accessToken: String,
        @Path("traceId") traceId: Int,
        @Part images: List<MultipartBody.Part>
    ): Call<Unit>

    // 발자국 측정 완료
    @PATCH("trace/measure-complete")
    fun measureComplete(
        @Header("Authorization") accessToken: String,
        @Body trace: Trace
    ): Call<Unit>

    // 발자국 삭제
    @DELETE("trace/{traceId}")
    fun deleteTrace(
        @Header("Authorization") accessToken: String,
        @Path("traceId") traceId: Int
    ): Call<Unit>
}