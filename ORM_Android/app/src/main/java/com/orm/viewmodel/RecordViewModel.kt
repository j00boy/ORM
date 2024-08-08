package com.orm.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orm.data.model.Record
import com.orm.data.repository.RecordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(
    private val recordRepository: RecordRepository
) : ViewModel() {
    private val _record = MutableLiveData<Record>()
    val record: LiveData<Record> get() = _record

    private val _recordId = MutableLiveData<Long>()
    val recordId: LiveData<Long> get() = _recordId

    fun getRecord(id: Long) {
        viewModelScope.launch {
            val record = recordRepository.getRecord(id)
            _record.postValue(record)
        }
    }

    fun insertRecord(record: Record) {
        viewModelScope.launch {
            val createId: Long = recordRepository.insertRecord(record)
            _recordId.postValue(createId)
        }
    }

    fun deleteRecord(record: Record) {
        viewModelScope.launch {
            recordRepository.deleteRecord(record)
        }
    }
}