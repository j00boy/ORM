package com.orm.data.model

data class Mountain(
    val id: Int,
    val name: String,
    val address: String,
    val imageSrc: String,
    val altitude: Double,
    val description: String
)