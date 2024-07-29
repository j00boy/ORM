package com.orm.data.repository

import android.util.Log
import com.google.gson.Gson
import com.orm.data.api.ClubService
import com.orm.data.model.ApproveClub
import com.orm.data.model.Club
import com.orm.data.model.RequestMember
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject

class ClubRepository @Inject constructor(
    private val clubService: ClubService
) {
    suspend fun getClubs(): List<Club> {
        return withContext(Dispatchers.IO) {
            val isConnected = true
            (if (isConnected) {
                val response = clubService.getClubs().execute()
                if (response.isSuccessful) {
                    response.body() ?: emptyList()
                } else {
                    emptyList()
                }
            } else {
                emptyList()
            })
        }
    }

    suspend fun getMembers(accessToken: String, clubId: Int): Map<String, List<Any?>> {
        return withContext(Dispatchers.IO) {
            val resultMap: MutableMap<String, List<Any?>> = mutableMapOf()
            val response = clubService.getMembers(accessToken, clubId).execute()
            if (response.isSuccessful) {
                val responseBody = response.body()
                val members = responseBody?.get("members") ?: emptyList()
                val requestMembers = responseBody?.get("requestMembers") ?: emptyList()
                resultMap["members"] = members
                resultMap["requestMembers"] = requestMembers
            } else {
                resultMap["members"] = emptyList()
                resultMap["requestMembers"] = emptyList()
            }

            resultMap
        }
    }


    suspend fun approveClubs(
        accessToken: String,
        approveClub: ApproveClub
    ): Boolean {
        return withContext(Dispatchers.IO) {
                val response =clubService.approveClubs(accessToken, approveClub).execute()
                response.isSuccessful
        }
    }

    suspend fun leaveClubs(
        accessToken: String,
        clubId: Int,
        userId: Int
    ): Boolean {
        return withContext(Dispatchers.IO) {

                val response = clubService.leaveClubs(accessToken, clubId, userId).execute()
                response.isSuccessful
        }
    }

    suspend fun applyClubs(
        accessToken: String,
        requestMember: RequestMember
    ): Boolean {
        return withContext(Dispatchers.IO) {

                val response = clubService.applyClubs(accessToken, requestMember).execute()
                response.isSuccessful
        }
    }

    suspend fun createClubs(
        accessToken: String,
        createClub: RequestBody,
        imgFile: MultipartBody.Part
    ): Int? {
        return withContext(Dispatchers.IO) {
            try {
                val response = clubService.createClubs(accessToken, createClub, imgFile).execute()
                if (response.isSuccessful) {
                    val responseBody = response.body()?.string()
                    responseBody?.toIntOrNull()
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun checkDuplicateClubs(
        accessToken: String,
        name: String
    ): Boolean? {
        return withContext(Dispatchers.IO) {
            try {
                val response = clubService.checkDuplicateClubs(accessToken, name).execute()
                if (response.isSuccessful) {
                    true
                } else {
                    false
                }
            } catch (e: Exception) {
                false
            }
        }
    }
}
