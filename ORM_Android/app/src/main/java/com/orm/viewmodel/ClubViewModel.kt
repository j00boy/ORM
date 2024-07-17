package com.orm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orm.data.model.Club
import com.orm.data.repository.ClubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClubViewModel @Inject constructor(
    private val clubRepository: ClubRepository,
) : ViewModel() {

    private val _clubs = MutableLiveData<List<Club>>()
    val clubs: LiveData<List<Club>> get() = _clubs

    fun getClubs() {
        viewModelScope.launch {
            _clubs.value = clubRepository.getClubs()
        }
    }
}