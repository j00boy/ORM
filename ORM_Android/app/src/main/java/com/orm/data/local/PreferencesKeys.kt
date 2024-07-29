package com.orm.data.local

import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val tokenString = stringPreferencesKey("tokenStringPreferenceKey")
    val userId = stringPreferencesKey("userIdPreferenceKey")
    val nickname = stringPreferencesKey("nicknamePreferenceKey")
    val imageSrc = stringPreferencesKey("imageSrcPreferenceKey")
}