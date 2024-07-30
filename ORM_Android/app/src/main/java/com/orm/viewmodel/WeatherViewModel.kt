package com.orm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orm.data.model.Weather
import com.orm.data.model.club.Club
import com.orm.data.repository.WeatherRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
): ViewModel() {

    private val _weather = MutableLiveData<List<Weather.WeatherItem>>()
    val weather: LiveData<List<Weather.WeatherItem>> get() = _weather

    fun getWeather(nx: String, ny: String, baseDate: String, baseTime: String) {
        viewModelScope.launch {
            val weatherItems = weatherRepository.getWeather(nx, ny, baseDate, baseTime)
            _weather.postValue(weatherItems)
        }
    }
}