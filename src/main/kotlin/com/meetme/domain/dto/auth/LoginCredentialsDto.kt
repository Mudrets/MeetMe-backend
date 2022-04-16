package com.meetme.domain.dto.auth

/**
 * Data transfer object с данными для входа пользователя.
 */
data class LoginCredentialsDto(
    /**
     * Электронная почта.
     */
    val email: String,
    /**
     * Пароль.
     */
    val password: String,
)