package com.orm.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.orm.data.model.Mountain
import com.orm.data.model.Point
import com.orm.data.model.Trail
import com.orm.data.repository.MountainRepository
import com.orm.util.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MountainViewModel @Inject constructor(
    private val mountainRepository: MountainRepository,
) : ViewModel() {

    private val _mountain = MutableLiveData<Mountain?>()
    val mountain: LiveData<Mountain?> get() = _mountain

    private val _mountains = MutableLiveData<List<Mountain>?>()
    val mountains: LiveData<List<Mountain>?> get() = _mountains

    private val _points = MutableLiveData<List<Point>?>()
    val points: LiveData<List<Point>?> get() = _points

    private val _trail = MutableLiveData<Trail?>()
    val trail: LiveData<Trail?> get() = _trail

    fun fetchMountainByName(name: String) {
        viewModelScope.launch {
            val mountains = mountainRepository.getMountainByName(name)
            _mountains.postValue(mountains)
        }
    }

    fun fetchMountainById(id: Int) {
        viewModelScope.launch {
            val mountain = mountainRepository.getMountainById(id, true)
            _mountain.postValue(mountain)
        }
    }

    fun fetchMountainById(id: Int, trailContaining: Boolean) {
        viewModelScope.launch {
            val mountain = mountainRepository.getMountainById(id, trailContaining)
            _mountain.postValue(mountain)
        }
    }

    fun fetchMountainsTop() {
        viewModelScope.launch {
            val mountains = mountainRepository.getMountainsTop()
            _mountains.postValue(mountains)
        }
    }

    fun fetchMountainsAll() {
        viewModelScope.launch {
            val mountains = mountainRepository.getMountainsAll()
            _mountains.postValue(mountains)
        }
    }

    fun fetchTrailById(trailId: Int) {
        viewModelScope.launch {
            val trail = mountainRepository.getTrailById(trailId)
            if (trail != null) {
                _points.postValue(trail.trailDetails)
            } else {
                _points.postValue(emptyList())
            }
        }
    }
}