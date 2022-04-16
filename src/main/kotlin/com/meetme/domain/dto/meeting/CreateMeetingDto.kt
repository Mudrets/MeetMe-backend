package com.meetme.domain.dto.meeting

/**
 * Data transfer object для создания нового мероприятия.
 */
data class CreateMeetingDto(
    /**
     * Идентификатор администратора.
     */
    val adminId: Long,
    /**
     * Название мероприятия.
     */
    val name: String,
    /**
     * Описание меропритяия.
     */
    val description: String? = null,
    /**
     * Интересы мероприятия.
     */
    val interests: Set<String> = setOf(),
    /**
     * Место проведения мероприятия.
     */
    val location: String? = null,
    /**
     * Проводится ли мероприятие в онлайн формате.
     */
    val isOnline: Boolean = false,
    /**
     * Ялвяется ли мероприятие приватным.
     */
    val isPrivate: Boolean = false,
    /**
     * Дата начала проведения меропритяия.
     */
    val startDate: String = "",
    /**
     * Дата окончания проведения мероприятия.
     */
    val endDate: String? = null,
    /**
     * Максимальное количество участников мероприятия.
     */
    val maxNumberOfParticipants: Int = 1,
)