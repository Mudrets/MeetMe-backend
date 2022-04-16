package com.meetme.domain.dto.goup

/**
 * Data transfer object для создания новой группы.
 */
data class CreateGroupDto(
    /**
     * Идентификатор админа группы.
     */
    val adminId: Long,
    /**
     * Название группы.
     */
    val name: String,
    /**
     * Описание группы.
     */
    val description: String = "",
    /**
     * Интересы группы.
     */
    val interests: Set<String> = setOf(),
    /**
     * Является ли группа приватной.
     */
    val isPrivate: Boolean = false,
)