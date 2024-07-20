package com.orm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orm.data.model.Mountain
import com.orm.data.model.Point
import com.orm.data.repository.MountainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MountainViewModel @Inject constructor(
    private val mountainRepository: MountainRepository,
) : ViewModel() {

    private val _mountain = MutableLiveData<Mountain?>()
    val mountain: LiveData<Mountain?> get() = _mountain

    private val _mountains = MutableLiveData<List<Mountain>>()
    val mountains: LiveData<List<Mountain>> get() = _mountains

    private val _points = MutableLiveData<List<Point>>()
    val points: LiveData<List<Point>> get() = _points

    fun fetchMountainByName(name: String) {
        viewModelScope.launch {
            val mountains = mountainRepository.getMountainByName(name)
            _mountains.postValue(mountains)
        }
    }

    fun fetchMountainById(id: Int) {
        viewModelScope.launch {
            val mountain = mountainRepository.getMountainById(id)
            _mountain.postValue(mountain)
        }
    }
    fun fetchMountainByRouteId(routeId: Int) {
        viewModelScope.launch {
            val points = mountainRepository.getMountainByRouteId(routeId)
            _points.postValue(points)
        }
    }
}