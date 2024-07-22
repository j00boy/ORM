package com.orm.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
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
        getAccessToken()
    }

    fun loginKaKao(code: String) {
        viewModelScope.launch {
            try {
                val user = userRepository.loginKakao(code)
                Log.d("UserViewModel", "User: $user")
                _user.postValue(user)
            } catch (e: Exception) {
                Log.e("UserViewModel", "Login failed: ${e.message}", e)
            }
        }
    }

    private fun getAccessToken() {
        viewModelScope.launch {
            val token = userRepository.getAccessToken()
            if (token.isEmpty()) {
                Log.d("UserViewModel", "token is empty")
            } else {
                _token.postValue(token)
                Log.d("UserViewModel", "token= $_token")
            }
        }
    }
}