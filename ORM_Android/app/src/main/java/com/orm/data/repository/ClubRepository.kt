package com.orm.data.repository

import com.orm.data.api.ClubService
import com.orm.data.local.ClubDao
import com.orm.data.model.Club
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
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
}