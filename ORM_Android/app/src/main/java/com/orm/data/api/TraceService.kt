package com.orm.data.api

import com.orm.data.model.Trace
import okhttp3.MultipartBody
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
    @FormUrlEncoded
    @POST("trace/register")
    fun registerTrace(
        @Header("Authorization") accessToken: String,
        @Field("title") title: String,
        @Field("mountainId") mountainId: Int,
        @Field("createDate") createDate: String,
        @Field("routeId") routeId: Int
    ): Call<Void>

    // 발자국 수정 (측정 전)
    @FormUrlEncoded
    @PATCH("trace/update/before-measure")
    fun updateBeforeMeasure(
        @Header("Authorization") accessToken: String,
        @Field("title") title: String,
        @Field("mountainId") mountainId: Int,
        @Field("routeId") routeId: Int,
        @Field("createDate") createDate: String
    ): Call<Void>

    // 발자국 수정 (측정 후 - 제목만)
    @FormUrlEncoded
    @PATCH("trace/update/after-measure")
    fun updateAfterMeasure(
        @Header("Authorization") accessToken: String,
        @Field("title") title: String
    ): Call<Void>

    // 발자국 수정 (측정 후 - 이미지)
    @Multipart
    @PATCH("trace/update/images")
    fun updateImages(
        @Header("Authorization") accessToken: String,
        @Part images: List<MultipartBody.Part>
    ): Call<Void>

    // 발자국 측정 완료
    @PATCH("trace/measure-complete")
    fun measureComplete(
        @Header("Authorization") accessToken: String,
        @Body trace: Trace
    ): Call<Void>

    // 발자국 삭제
    @DELETE("trace/{traceId}")
    fun deleteTrace(
        @Header("Authorization") accessToken: String,
        @Path("traceId") traceId: Int
    ): Call<Void>
}