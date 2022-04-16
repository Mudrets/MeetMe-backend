package com.meetme.domain.dto.auth

/**
 * Data transfer object с информацией о пользователе, необходимой
 * клиентскому приложению.
 */
data class UserDto(
    /**
     * Идентификатор пользователя.
     */
    val id: Long,
    /**
     * Полное имя пользователя.
     */
    val fullName: String,
    /**
     * Ссылки на социальные сети пользователя.
     */
    val links: Map<String, String> = mapOf(),
    /**
     * Интересы пользователя.
     */
    val interests: List<String> = listOf(),
    /**
     * Ссылка на фотографию пользователя.
     */
    val photoUrl: String? = null,
    /**
     * Электронная почта пользователя.
     */
    val email: String? = null,
    /**
     * Телефон пользователя.
     */
    val telephone: String? = null,
    /**
     * Описание профиля пользователя.
     */
    val description: String? = null,
)