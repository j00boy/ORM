package com.orm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orm.data.local.dao.ClubDao
import com.orm.data.model.club.ClubApprove
import com.orm.data.model.club.Club
import com.orm.data.model.RequestMember
import com.orm.data.repository.ClubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class ClubViewModel @Inject constructor(
    private val clubRepository: ClubRepository,
    private val clubDao: ClubDao,
) : ViewModel() {

    private val _clubs = MutableLiveData<List<Club>>()
    val clubs: LiveData<List<Club>> get() = _clubs

    private val _members = MutableLiveData<Map<String, List<Any?>>>()
    val members: LiveData<Map<String, List<Any?>>> get() = _members

    private val _isOperationSuccessful = MutableLiveData<Boolean>()
    val isOperationSuccessful: LiveData<Boolean> get() = _isOperationSuccessful

    private val _clubId = MutableLiveData<Int?>()
    val createdClubId: LiveData<Int?> get() = _clubId


    fun getClubs() {
        viewModelScope.launch {
            val clubs = clubRepository.getClubs()
            _clubs.postValue(clubs)
        }
    }

    fun getMembers(clubId: Int) {
        viewModelScope.launch {
            val members = clubRepository.getMembers(clubId)
            _members.postValue(members)
        }
    }

    fun approveClubs(approveClub: ClubApprove) {
        viewModelScope.launch {
            try {
                val success = clubRepository.approveClubs(approveClub)
                _isOperationSuccessful.postValue(success)
            } catch (e: Exception) {
                e.printStackTrace()
                _isOperationSuccessful.postValue(false)
            }
        }
    }

    fun leaveClubs(clubId: Int, userId: Int) {
        viewModelScope.launch {
            try {
                val success = clubRepository.leaveClubs(clubId, userId)
                _isOperationSuccessful.postValue(success)
            } catch (e: Exception) {
                e.printStackTrace()
                _isOperationSuccessful.postValue(false)
            }
        }
    }

    fun applyClubs(requestMember: RequestMember) {
        viewModelScope.launch {
            try {
                val success = clubRepository.applyClubs(requestMember)
                _isOperationSuccessful.postValue(success)
            } catch (e: Exception) {
                e.printStackTrace()
                _isOperationSuccessful.postValue(false)
            }
        }
    }


    fun createClubs(createClub: RequestBody, imgFile: MultipartBody.Part) {
        viewModelScope.launch {
            try {
                val clubId = clubRepository.createClubs(createClub, imgFile)
                _clubId.postValue(clubId)
                _isOperationSuccessful.postValue(clubId != null)
            } catch (e: Exception) {
                e.printStackTrace()
                _clubId.postValue(null)
                _isOperationSuccessful.postValue(false)
            }
        }
    }

    fun checkDuplicateClubs(clubName: String) {
        viewModelScope.launch {
            try {
                val isDuplicate = clubRepository.checkDuplicateClubs(clubName)
                _isOperationSuccessful.postValue(isDuplicate ?: false)
            } catch (e: Exception) {
                e.printStackTrace()
                _isOperationSuccessful.postValue(false)
            }
        }
    }

}