package com.orm.data.repository

import android.util.Log
import com.orm.data.api.TraceService
import com.orm.data.local.dao.TraceDao
import com.orm.data.model.Trace
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TraceRepository @Inject constructor(
    private val traceDao: TraceDao,
    private val traceService: TraceService,
) {
    suspend fun getAllTraces(): List<Trace> {
        return withContext(Dispatchers.IO) {
            traceDao.getAllTraces()
        }
    }

    suspend fun insertTrace(trace: Trace) {
        withContext(Dispatchers.IO) {
            traceDao.insertTrace(trace)
        }
    }

    suspend fun deleteTrace(trace: Trace) {
        withContext(Dispatchers.IO) {
            traceDao.deleteTrace(trace)
        }
    }

    suspend fun getTrace(id: Int): Trace {
        return withContext(Dispatchers.IO) {
            traceDao.getTrace(id)
        }
    }

    suspend fun createTrace(trace: Trace): Trace? {
        return withContext(Dispatchers.IO) {
            try {
                val response = traceService.createTrace(trace).execute()
                if (response.isSuccessful) {
                    response.body()
                } else {
                    null
                }
            } catch (e: Exception) {
                Log.e("TraceRepository", "Error creating trace", e)
                null
            }
        }
    }

    suspend fun updateTrace(trace: Trace) {
        return withContext(Dispatchers.IO) {
            try {
                val response = traceService.updateTrace(trace).execute()
                if (response.isSuccessful) {
                    response.body()
                } else {
                    null
                }
            } catch (e: Exception) {
                Log.e("TraceRepository", "Error updating trace", e)
                null
            }
        }
    }

    suspend fun measureComplete(trace: Trace): Unit? {
        return withContext(Dispatchers.IO) {
            try {
                val response = traceService.measureComplete(trace).execute()
                if (response.isSuccessful) {
                    response.body()
                } else {
                    null
                }
            } catch (e: Exception) {
                Log.e("TraceRepository", "Error measuring complete trace", e)
                null
            }
        }
    }

    suspend fun deleteTrace(traceId: Int): Unit? {
        return withContext(Dispatchers.IO) {
            try {
                val response = traceService.deleteTrace(traceId).execute()
                if (response.isSuccessful) {
                    response.body()
                } else {
                    null
                }
            } catch (e: Exception) {
                Log.e("TraceRepository", "Error deleting trace", e)
                null
            }
        }
    }
}
