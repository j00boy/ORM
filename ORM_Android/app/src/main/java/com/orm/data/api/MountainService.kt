package com.orm.data.api

import com.orm.data.model.Mountain
import com.orm.data.model.Point
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MountainService {
    // 등산로 ID로 검색
    @GET("mountains/route/{routesID}")
    fun getRoute(@Path("routesID") routesID: Int): Call<List<Point>>

    // 산 ID로 불러오기
    @GET("mountains/{mountainID}")
    fun getMountainById(@Path("mountainID") mountainID: Int): Call<Mountain>

    // 산 이름으로 검색
    @GET("mountains/search")
    fun searchMountains(@Query("name") name: String): Call<List<Mountain>>
}