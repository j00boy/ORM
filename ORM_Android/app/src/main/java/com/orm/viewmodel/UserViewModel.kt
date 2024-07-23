package com.orm.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orm.data.model.User
import com.orm.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    private val _token = MutableLiveData<String>()
    val token: LiveData<String> get() = _token

    init {
        _token.value = ""
    }

    fun loginKakao(code: String) {
        viewModelScope.launch {
            try {
                val user = userRepository.loginKakao(code)
                _user.postValue(user)
                Log.d("UserViewModel", "User: $user")
                getAccessToken()
            } catch (e: Exception) {
                Log.e("UserViewModel", "Login failed: ${e.message}", e)
            }
        }
    }

    fun loginAuto() {
        Log.d("UserViewModel", "loginAuto")
        viewModelScope.launch {
            try {
                val user = userRepository.loginAuto()
                _user.postValue(user)
                getAccessToken()
            } catch (e: Exception) {
                Log.e("UserViewModel", "Login failed: ${e.message}", e)
            }
        }
    }

    fun getAccessToken() {
        Log.d("UserViewModel", "getAccessToken")
        viewModelScope.launch {
            val token: String = userRepository.getAccessToken()
            if (token.isEmpty()) {
                Log.d("UserViewModel", "token is empty")
            } else {
                _token.postValue(token)
                Log.d("UserViewModel", "token= $_token")
            }
        }
    }
}