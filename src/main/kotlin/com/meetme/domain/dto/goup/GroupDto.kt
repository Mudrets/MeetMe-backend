package com.meetme.domain.dto.goup

/**
 * Data transfer object с информацией о группе, необходимой клиентскому приложению.
 */
data class GroupDto(
    /**
     * Идентификатор группы.
     */
    val id: Long,
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
    val description: String,
    /**
     * Ссылка на фотографию группы.
     */
    val photoUrl: String?,
    /**
     * Ялвяется ли группы приватной.
     */
    val isPrivate: Boolean,
    /**
     * Интересы группы.
     */
    val interests: List<String> = listOf(),
)