package com.orm.data.repository

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import com.orm.data.api.UserService
import com.orm.data.local.PreferencesKeys
import com.orm.data.model.User
import com.orm.util.dataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userService: UserService,
    @ApplicationContext private val context: Context,
) {
    suspend fun loginKakao(code: String): User? {
        return withContext(Dispatchers.IO) {
            try {
                val response = userService.loginKakao(code).execute()
                if (response.isSuccessful) {
                    saveAccessToken(response.headers().get("accessToken").toString())
                    response.body() ?: throw Exception("Login failed")
                } else {
                    throw Exception("Login failed ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("UserRepository", "Error during Kakao login", e)
                null
            }
        }
    }

    suspend fun loginAuto(): User? {
        return withContext(Dispatchers.IO) {
            try {
                val response = userService.loginAuto().execute()
                if (response.isSuccessful) {
                    Log.d("test", response.headers()["accessToken"].toString())
                    saveAccessToken(response.headers()["accessToken"].toString())

                    val body = response.body() ?: throw Exception("Login failed")
                    saveUserInfo(body)
                    body
                } else {
                    throw Exception(response.errorBody()?.string())
                }
            } catch (e: Exception) {
                Log.e("UserRepository", "Error during auto login", e)
                null
            }
        }
    }

    suspend fun registerFirebaseToken(firebaseToken: String) {
        return withContext(Dispatchers.IO) {
            try {
                val response = userService.registerFirebaseToken(firebaseToken).execute()
                if (response.isSuccessful) {
                    Log.d("UserRepository", "registerFirebaseToken: $firebaseToken")
                } else {
                    throw Exception(response.errorBody()?.string())
                }
            } catch (e: Exception) {
                Log.d("UserRepository", "Error during auto login", e)
            }
        }
    }

    private suspend fun saveAccessToken(accessToken: String) {
        try {
            context.dataStore.edit { preferences ->
                preferences[PreferencesKeys.tokenString] = accessToken
            }
            Log.d("UserRepository", "saveAccessToken: $accessToken")
        } catch (e: Exception) {
            Log.e("UserRepository", "Error saving access token", e)
        }
    }

    suspend fun getAccessToken(): String {
        return try {
            val accessToken: Flow<String> = context.dataStore.data.map { preferences ->
                preferences[PreferencesKeys.tokenString] ?: ""
            }
            Log.d("UserRepository", "getAccessToken: ${accessToken.first()}")
            accessToken.first()
        } catch (e: Exception) {
            Log.e("UserRepository", "Error getting access token", e)
            ""
        }
    }

    suspend fun deleteAccessToken() {
        try {
            context.dataStore.edit { preferences ->
                preferences.remove(PreferencesKeys.tokenString)
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Error deleting access token", e)
        }
    }

    private suspend fun saveUserInfo(user: User) {
        try {
            context.dataStore.edit { preferences ->
                preferences[PreferencesKeys.userId] = user.userId
                preferences[PreferencesKeys.imageSrc] = user.imageSrc
                preferences[PreferencesKeys.nickname] = user.nickname
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Error saving user info", e)
        }
    }

    suspend fun getUserInfo(): User? {
        return try {
            val userId: Flow<String> = context.dataStore.data.map { preferences ->
                preferences[PreferencesKeys.userId] ?: ""
            }

            val imageSrc: Flow<String> = context.dataStore.data.map { preferences ->
                preferences[PreferencesKeys.imageSrc] ?: ""
            }

            val nickname: Flow<String> = context.dataStore.data.map { preferences ->
                preferences[PreferencesKeys.nickname] ?: ""

            }

            Log.d(
                "UserRepository",
                "userInfo: ${userId.first() + imageSrc.first() + nickname.first()}"
            )
            User(userId.first(), imageSrc.first(), nickname.first())
        } catch (e: Exception) {
            Log.e("UserRepository", "Error getting user info", e)
            null
        }
    }

    suspend fun deleteUserInfo() {
        try {
            context.dataStore.edit { preferences ->
                preferences.remove(PreferencesKeys.userId)
                preferences.remove(PreferencesKeys.imageSrc)
                preferences.remove(PreferencesKeys.nickname)
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Error deleting user info", e)
        }

        try {
            withContext(Dispatchers.IO) {
                userService.deleteUser().execute()
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Error during user deletion", e)
        }
    }
}
