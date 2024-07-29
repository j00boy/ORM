package com.orm.data.repository

import android.util.Log
import com.orm.data.api.MountainService
import com.orm.data.model.Mountain
import com.orm.data.model.Point
import com.orm.data.model.Trail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

    suspend fun getTrailById(trailId: Int): Trail? {
        return withContext(Dispatchers.IO) {
            val response = mountainService.getTrail(trailId).execute()
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        }
    }

    suspend fun getMountainsTop(): List<Mountain>? {
        return withContext(Dispatchers.IO) {
            val response = mountainService.getMountainsTop().execute()

            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        }
    }
}