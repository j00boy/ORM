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
import com.orm.data.local.PreferencesKeys
import com.orm.data.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject


// datastore
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = BuildConfig.DATASTORE_NAME,
)

class UserRepository @Inject constructor(
    private val userService: UserService,
    @ApplicationContext private val context: Context,
) {
    suspend fun loginKakao(code: String): User {
        return withContext(Dispatchers.IO) {
            val response = userService.loginKakao(code).execute()

            if (response.isSuccessful) {
                saveAccessToken(response.headers().get("accessToken").toString())
                response.body() ?: throw Exception("Login failed")
            } else {
                throw Exception("Login failed ${response.errorBody()?.string()}")
            }
        }
    }

    suspend fun loginAuto(): User {
        return withContext(Dispatchers.IO) {
            val response = userService.loginAuto().execute()
            if (response.isSuccessful) {
                Log.d("test", response.headers().get("accessToken").toString())
                saveAccessToken(response.headers().get("accessToken").toString())
                response.body() ?: throw Exception("Login failed")
            } else {
                throw Exception(response.errorBody()?.string())
            }
        }
    }

    private suspend fun saveAccessToken(accessToken: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.tokenString] = accessToken
        }
        Log.d("UserRepository", "saveAccessToken: $accessToken")
    }

    suspend fun getAccessToken(): String {
        val accessToken: Flow<String> = context.dataStore.data.map { preferences ->
            preferences[PreferencesKeys.tokenString] ?: ""
        }
        Log.d("UserRepository", "getAccessToken: ${accessToken.first()}")
        return accessToken.first()
    }
}