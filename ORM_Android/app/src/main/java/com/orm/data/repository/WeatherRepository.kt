package com.orm.data.repository

import com.orm.data.api.WeatherService
import com.orm.data.model.Weather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherService: WeatherService
){
    suspend fun getWeather(nx: String, ny: String, baseDate: String, baseTime: String): List<Weather.WeatherItem> {
        return withContext(Dispatchers.IO) {
            val response = weatherService.getWeather(
                nx = nx,
                ny = ny,
                baseDate = baseDate
            )

            if (response.isSuccessful) {
                response.body()?.response?.body?.items?.item ?: emptyList()
            } else {
                emptyList()
            }
        }
    }
}