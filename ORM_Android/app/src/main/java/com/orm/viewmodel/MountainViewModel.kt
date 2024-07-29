package com.orm.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orm.data.model.Mountain
import com.orm.data.model.Point
import com.orm.data.model.Trail
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

    private val _mountains = MutableLiveData<List<Mountain>?>()
    val mountains: LiveData<List<Mountain>?> get() = _mountains

    private val _points = MutableLiveData<List<Point>>()
    val points: LiveData<List<Point>> get() = _points

    private val _trail = MutableLiveData<Trail?>()
    val trail: LiveData<Trail?> get() = _trail

    fun fetchMountainByName(name: String) {
        viewModelScope.launch {
            val mountains = mountainRepository.getMountainByName(name)
            Log.e("fetchMountainByName", mountains.toString())
//            val mountains = listOf(
//                Mountain(
//                    id = 1,
//                    name = "산1",
//                    code = 1,
//                    address = "주소1",
//                    imageSrc = "https://gongu.copyright.or.kr/gongu/wrt/cmmn/wrtFileImageView.do?wrtSn=13262118&filePath=L2Rpc2sxL25ld2RhdGEvMjAyMC8yMS9DTFMxMDAwNi82MmZhMWExMy03ZjRmLTQ1NWMtYTZlNy02ZTk2YjhjMjBkYTk=&thumbAt=Y&thumbSe=b_tbumb&wrtTy=10006",
//                    desc = "설명1",
//                    height = 1.0,
//                    trails = listOf()
//                )
//            )
            _mountains.postValue(mountains)
        }
    }

    fun fetchMountainById(id: Int) {
        viewModelScope.launch {
            val mountain = mountainRepository.getMountainById(id)
            _mountain.postValue(mountain)
        }
    }
    fun fetchMountainByRouteId(trailId: Int) {
        viewModelScope.launch {
            val trail = mountainRepository.getTrailById(trailId)
            _trail.postValue(trail)
        }
    }
    fun fetchMountainsTop() {
        viewModelScope.launch {
            val mountains = mountainRepository.getMountainsTop()
            _mountains.postValue(mountains)
        }
    }
}