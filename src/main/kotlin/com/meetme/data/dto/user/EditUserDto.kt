package com.meetme.data.dto.user

data class EditUserDto(
    val fullName: String,
    val description: String,
    val mediaLinks: Map<String, String> = mapOf(),
    val interests: Set<String> = setOf(),
)
