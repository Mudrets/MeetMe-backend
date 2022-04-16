package com.meetme.domain.dto.auth

/**
 * Data transfer object для регистрации нового пользователя.
 */
data class RegisterCredentialsDto(
    /**
     * Полное имя пользователя.
     */
    val fullName: String,
    /**
     * Электронная почта пользователя.
     */
    val email: String,
    /**
     * Пароль пользователя.
     */
    val password: String,
)