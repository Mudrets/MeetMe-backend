package com.meetme.domain.dto.goup

/**
 * Data transfer object для изменения группы.
 */
data class UpdateGroupDto(
    /**
     * Новое название группы.
     */
    val name: String,
    /**
     * Новое описание группы.
     */
    val description: String,
    /**
     * Новая ссылка на фотографию группы.
     */
    val photoUrl: String?,
    /**
     * Новый статус приватности группы.
     */
    val isPrivate: Boolean,
    /**
     * Новый список интересов группы.
     */
    val interests: Set<String> = setOf(),
    /**
     * Ссылки на социальные сети группы.
     */
    val socialMediaLinks: Map<String, String> = mapOf()
)
