package com.meetme.domain.dto.meeting

/**
 * Data transfer object для редактирования мероприятия.
 */
data class UpdateMeetingDto(
    /**
     * Название мероприятия.
     */
    val name: String,
    /**
     * Описание мероприятия.
     */
    val description: String,
    /**
     * Дата и время начала проведения мероприятия.
     */
    val startDate: String,
    /**
     * Дата и время окончания проведения мероприятия.
     */
    val endDate: String? = null,
    /**
     * Проводится ли мероприятияе в онлайн формате.
     */
    val isOnline: Boolean,
    /**
     * Место проведения мероприятия.
     */
    val locate: String,
    /**
     * Максимальное количество участников мероприятия.
     */
    val maxNumberOfParticipants: Int,
    /**
     * Интересы мероприятия.
     */
    val interests: Set<String> = setOf()
)
