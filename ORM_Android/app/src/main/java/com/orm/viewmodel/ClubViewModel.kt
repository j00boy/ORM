package com.orm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orm.data.local.dao.ClubDao
import com.orm.data.model.ApproveClub
import com.orm.data.model.Club
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
    val members: LiveData<Map<String, List<Any?>>>
        get() = _members

    private val _isOperationSuccessful = MutableLiveData<Boolean>()
    val isOperationSuccessful: LiveData<Boolean>
        get() = _isOperationSuccessful

    private val _clubId = MutableLiveData<Int?>()
    val createdClubId: LiveData<Int?>
        get() = _clubId

    init {
        getClubs()
    }

    fun getClubs() {
        viewModelScope.launch {
            val clubs = clubRepository.getClubs()
            _clubs.postValue(clubs)
        }
    }

    fun getMembers(accessToken: String, clubId: Int) {
        viewModelScope.launch {
            val members = clubRepository.getMembers(accessToken, clubId)
            _members.postValue(members)
        }
    }

    fun approveClubs(
        accessToken: String,
        approveClub: ApproveClub
    ) {
        viewModelScope.launch {
            try {
                val success = clubRepository.approveClubs(accessToken, approveClub)
                _isOperationSuccessful.postValue(success)
            } catch (e: Exception) {
                e.printStackTrace()
                _isOperationSuccessful.postValue(false)
            }
        }
    }

    fun leaveClubs(
        accessToken: String,
        clubId: Int,
        userId: Int
    ) {
        viewModelScope.launch {
            try {
                val success = clubRepository.leaveClubs(accessToken, clubId, userId)
                _isOperationSuccessful.postValue(success)
            } catch (e: Exception) {
                e.printStackTrace()
                _isOperationSuccessful.postValue(false)
            }
        }
    }

    fun applyClubs(
        accessToken: String,
        requestMember: RequestMember
    ) {
        viewModelScope.launch {
            try {
                val success = clubRepository.applyClubs(accessToken, requestMember)
                _isOperationSuccessful.postValue(success)
            } catch (e: Exception) {
                e.printStackTrace()
                _isOperationSuccessful.postValue(false)
            }
        }
    }


    fun createClubs(
        accessToken: String,
        createClub: RequestBody,
        imgFile: MultipartBody.Part
    ) {
        viewModelScope.launch {
            try {
                val clubId = clubRepository.createClubs(accessToken, createClub, imgFile)
                _clubId.postValue(clubId)
                _isOperationSuccessful.postValue(clubId != null)
            } catch (e: Exception) {
                e.printStackTrace()
                _clubId.postValue(null)
                _isOperationSuccessful.postValue(false)
            }
        }
    }

    fun checkDuplicateClubs(
        accessToken: String,
        clubName: String) {
        viewModelScope.launch {
            try {
                val isDuplicate = clubRepository.checkDuplicateClubs(accessToken, clubName)
                _isOperationSuccessful.postValue(isDuplicate ?: false)
            } catch (e: Exception) {
                e.printStackTrace()
                _isOperationSuccessful.postValue(false)
            }
        }
    }

}