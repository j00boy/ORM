package com.orm.data.repository

import com.orm.data.local.dao.TraceDao
import com.orm.data.model.Trace
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TraceRepository @Inject constructor(
    private val traceDao: TraceDao,
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
}