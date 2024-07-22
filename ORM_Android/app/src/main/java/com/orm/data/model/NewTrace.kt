package com.orm.data.model

import java.time.LocalDateTime

data class NewTrace(
    val hikingDate: LocalDateTime,
    val id: Int,
    val mountainId: Int,
    val title: String,
    val trailId: Int
)