package com.orm.data.api

import com.orm.BuildConfig
import com.orm.data.model.Weather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst")
    fun getWeather(
        @Query("serviceKey") serviceKey: String = BuildConfig.WEATHER_API_KEY,
        @Query("dataType") dataType: String = "JSON",
        @Query("numOfRows") numOfRows: Int = 3,
        @Query("pageNo") pageNo: Int = 1,
        @Query("base_date") baseDate: String,
        @Query("base_time") baseTime: String = "0900",
        @Query("nx") nx: String,
        @Query("ny") ny: String
    ): Response<Weather>
}