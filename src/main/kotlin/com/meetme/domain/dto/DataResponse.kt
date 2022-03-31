package com.meetme.domain.dto

data class DataResponse<out T>(
    val message: String = "success",
    val appCode: Int = 0,
    val data: T? = null,
)
