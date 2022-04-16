package com.meetme.jwt

/**
 * Запрос на аутентификацию и получение JWT-токена.
 */
class UsernameAndPasswordAuthenticationRequest {
    /**
     * Username пользователя.
     */
    var username: String? = null

    /**
     * Пароль пользователя.
     */
    var password: String? = null
}