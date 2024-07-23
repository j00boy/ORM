package com.orm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orm.data.local.dao.TraceDao
import com.orm.data.model.Trace
import com.orm.data.repository.TraceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TraceViewModel @Inject constructor(
    private val traceRepository: TraceRepository,
    private val traceDao: TraceDao,
) : ViewModel() {

    private val _traces = MutableLiveData<List<Trace>>()
    val traces: LiveData<List<Trace>> get() = _traces

    private val _trace = MutableLiveData<Trace>()
    val trace: LiveData<Trace> get() = _trace

    init {
        getTraces()
    }

    private fun getTraces() {
        viewModelScope.launch {
            val traces = traceDao.getAllTraces()
            _traces.postValue(traces)
        }
    }

    fun getTrace(id: Int) {
        viewModelScope.launch {
            val trace = traceDao.getTrace(id)
            _trace.postValue(trace)
        }
    }

    fun insertTrace(trace: Trace) {
        viewModelScope.launch {
            traceDao.insertTrace(trace)

            val updateTraces = _traces.value?.toMutableList() ?: mutableListOf()
            updateTraces.add(trace)
            _traces.postValue(updateTraces)
        }
    }

    fun deleteTrace(trace: Trace) {
        viewModelScope.launch {
            traceDao.deleteTrace(trace)

            val updateTraces = _traces.value?.toMutableList()?.filter {
                it.id != trace.id
            } ?: mutableListOf()
            _traces.postValue(updateTraces)
        }
    }
}