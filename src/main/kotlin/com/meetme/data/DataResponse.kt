package com.meetme.data

data class DataResponse<out T>(
    val message: String = "success",
    val appCode: Int = 0,
    val data: T? = null,
)
