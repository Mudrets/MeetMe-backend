package com.meetme.domain.dto.user

/**
 * Data transfer object для редактирования пользователя.
 */
data class EditUserDto(
    /**
     * Полное имя пользователя.
     */
    val fullName: String,
    /**
     * Описание профиля пользователя.
     */
    val description: String,
    /**
     * Ссылки на социальные сети пользователя.
     */
    val mediaLinks: Map<String, String> = mapOf(),
    /**
     * Сет интересов пользователя.
     */
    val interests: Set<String> = setOf(),
)
