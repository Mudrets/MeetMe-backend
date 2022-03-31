package com.meetme.domain.dto.auth

data class LoginCredentialsDto(
    val email: String,
    val password: String,
)