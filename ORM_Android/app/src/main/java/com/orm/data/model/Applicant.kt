package com.orm.data.model

data class Applicant(
    val introduction: String,
    val userId: Int,
    val imageSrc: String?,
    val nickname: String,
)