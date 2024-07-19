package com.orm.data.repository

import com.orm.data.api.ClubService
import com.orm.data.api.MountainService
import com.orm.data.local.ClubDao
import com.orm.data.model.Club
import com.orm.data.model.Mountain
import com.orm.data.model.Point
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import javax.inject.Inject

class MountainRepository @Inject constructor(
    private val mountainService: MountainService,
) {
    suspend fun getMountainByName(name: String): List<Mountain> {
        return withContext(Dispatchers.IO) {
            val response = mountainService.searchMountains(name).execute()

            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        }
    }

    suspend fun getMountainById(id: Int): Mountain? {
        return withContext(Dispatchers.IO) {
            val response = mountainService.getMountainById(id).execute()

            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        }
    }

    suspend fun getMountainByRouteId(routeId: Int): List<Point> {
        return withContext(Dispatchers.IO) {
            val response = mountainService.getRoute(routeId).execute()

            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        }
    }
}