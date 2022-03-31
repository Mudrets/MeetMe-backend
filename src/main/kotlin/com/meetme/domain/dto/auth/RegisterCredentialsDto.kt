package com.meetme.domain.dto.auth

data class RegisterCredentialsDto(
    val fullName: String,
    val email: String,
    val password: String,

)