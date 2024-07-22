package com.orm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.orm.data.model.User
import com.orm.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel(){
    private val _user = MutableLiveData<User>()
    val user : LiveData<User> get() = _user
//
//    init {
//        getToken()
//    }

//    private fun getToken() {
//        viewModelScope.launch {
//            val token = userRepository.getAccessToken()
//
//            if (token.isEmpty()) {
//                Log.d("UserViewModel", "Token is empty")
//            } else {
//                Log.d("UserViewModel", token)
//            }
//        }
//    }

}