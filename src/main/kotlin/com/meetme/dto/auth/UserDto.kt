package com.meetme.dto.auth

data class UserDto(
    val id: Long,
    val fullName: String,
    val links: Map<String, String> = mapOf(),
    val interests: List<String> = listOf(),
    val photoUrl: String? = null,
    val email: String? = null,
    val telephone: String? = null,
    val description: String? = null,
)