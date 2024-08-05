package com.orm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class TrackViewModel : ViewModel() {
    private val _lanlngs = MutableLiveData<List<LatLng>>()
    val lanlngs: LiveData<List<LatLng>> get() = _lanlngs

    private val _distance = MutableLiveData<Double>()
    val distance: LiveData<Double> get() = _distance

    init {
        _lanlngs.value = emptyList()
    }

    fun updateLanlngs(newLatLng: LatLng) {
        val currentLanlngs = _lanlngs.value?.toMutableList() ?: mutableListOf()
        currentLanlngs.add(newLatLng)
        _lanlngs.value = currentLanlngs
    }

    fun clearLatLngs() {
        _lanlngs.value = emptyList()
    }
}
