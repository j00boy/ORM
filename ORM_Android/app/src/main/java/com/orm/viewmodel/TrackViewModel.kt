package com.orm.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.orm.data.model.Point

class TrackViewModel : ViewModel() {
    private val _points = MutableLiveData<List<Point>>()
    val points: LiveData<List<Point>> get() = _points

    private val _distance = MutableLiveData<Double>()
    val distance: LiveData<Double> get() = _distance

    init {
        _distance.value = 0.0
        _points.value = emptyList()
    }

    fun updatePoint(point: Point) {
        Log.d("TrackViewModel", "Updating point: $point")

        val currentPoints = _points.value?.toMutableList() ?: mutableListOf()
        currentPoints.add(point)
        _points.value = currentPoints

        if (currentPoints.size >= 2) {
            val lastPoint = currentPoints.last()
            val secondLastPoint = currentPoints[currentPoints.size - 2]
            val distance = Point.getDistance(lastPoint, secondLastPoint)

            val currentDistance = _distance.value ?: 0.0
            _distance.value = currentDistance + distance
        }
    }

    fun clearPoints() {
        _points.value = emptyList()
    }
}
