package com.orm.data.repository

import android.util.Log
import com.orm.data.api.ClubService
import com.orm.data.model.club.ClubApprove
import com.orm.data.model.club.Club
import com.orm.data.model.RequestMember
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class ClubRepository @Inject constructor(
    private val clubService: ClubService,
) {
    suspend fun getClubs(keyword: String, isMyClub: Boolean): List<Club> {
        return withContext(Dispatchers.IO) {
            try {
                val response =
                    clubService.getClubs(keyword = keyword, isMyClub = isMyClub).execute()
                if (response.isSuccessful) {
                    response.body() ?: emptyList()
                } else {
                    emptyList()
                }
            } catch (e: Exception) {
                Log.e("ClubRepository", "Error getting clubs", e)
                emptyList()
            }
        }
    }

    suspend fun getMembers(clubId: Int): Map<String, List<Any?>> {
        return withContext(Dispatchers.IO) {
            val resultMap: MutableMap<String, List<Any?>> = mutableMapOf()
            try {
                val response = clubService.getMembers(clubId).execute()
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
            } catch (e: Exception) {
                Log.e("ClubRepository", "Error getting members", e)
                resultMap["members"] = emptyList()
                resultMap["requestMembers"] = emptyList()
            }

            resultMap
        }
    }

    suspend fun approveClubs(approveClub: ClubApprove): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = clubService.approveClubs(approveClub).execute()
                response.isSuccessful
            } catch (e: Exception) {
                Log.e("ClubRepository", "Error approving clubs", e)
                false
            }
        }
    }

    suspend fun leaveClubs(clubId: Int, userId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = clubService.leaveClubs(clubId, userId).execute()
                response.isSuccessful
            } catch (e: Exception) {
                Log.e("ClubRepository", "Error leaving club", e)
                false
            }
        }
    }

    suspend fun applyClubs(requestMember: RequestMember): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = clubService.applyClubs(requestMember).execute()
                response.isSuccessful
            } catch (e: Exception) {
                Log.e("ClubRepository", "Error applying to club", e)
                false
            }
        }
    }

    suspend fun createClubs(createClub: RequestBody, imgFile: MultipartBody.Part): Int? {
        return withContext(Dispatchers.IO) {
            try {
                val response = clubService.createClubs(createClub, imgFile).execute()
                if (response.isSuccessful) {
                    val responseBody = response.body()?.string()
                    responseBody?.toIntOrNull()
                } else {
                    null
                }
            } catch (e: Exception) {
                Log.e("ClubRepository", "Error creating club", e)
                null
            }
        }
    }

    suspend fun checkDuplicateClubs(name: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = clubService.checkDuplicateClubs(name).execute()
                if (response.isSuccessful) {
                    response.body() ?: false
                } else {
                    false
                }
            } catch (e: Exception) {
                Log.e("ClubRepository", "Error checking duplicate clubs", e)
                false
            }
        }
    }
}
