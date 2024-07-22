package com.orm.data.repository

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.orm.BuildConfig
import com.orm.data.api.UserService
import com.orm.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject


//// datastore
//private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
//    name = BuildConfig.DATASTORE_NAME,
//)

class UserRepository @Inject constructor(
    private val userService: UserService,
) {
    suspend fun loginKakao(code: String): User {
        return withContext(Dispatchers.IO) {
            val response = userService.loginKakao(code).execute()

            Log.d("UserRepository", "Login response: $response")
            if (response.isSuccessful) {
                response.body() ?: throw Exception("Login failed")
            } else {
                throw Exception("Login failed")
            }
        }
    }

    suspend fun loginAuto(): User {
        return withContext(Dispatchers.IO) {
            val response = userService.loginAuto().execute()

            if (response.isSuccessful) {
                response.body() ?: throw Exception("Login failed")
            } else {
                throw Exception("Login failed")
            }
        }
    }

//    suspend fun saveAccessToken(accessToken: String) {
//        context.dataStore.edit { preferences ->
//            preferences[stringPreferencesKey("AccessToken")] = accessToken
//        }
//    }
//
//    suspend fun getAccessToken(): String {
//        val accessToken: Flow<String> = context.dataStore.data.map { preferences ->
//            preferences[stringPreferencesKey("AccessToken")] ?: ""
//        }
//        return accessToken.toString()
//    }
}