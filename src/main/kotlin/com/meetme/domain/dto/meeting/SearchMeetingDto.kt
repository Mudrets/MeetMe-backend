package com.meetme.domain.dto.meeting

import com.meetme.domain.dto.meeting.enums.MeetingType

/**
 * Data transfer object для поиска мероприятий.
 */
data class SearchMeetingDto(
    /**
     * Поисковой запрос.
     */
    val searchQuery: String = "",
    /**
     * Список интересов искомого мероприятия.
     */
    val interests: List<String> = listOf(),
    /**
     * Максимальное количество участников мероприятия.
     */
    val maxNumberOfParticipants: Int? = null,
    /**
     * Дата начала проведения мероприятия.
     */
    val startDate: String? = null,
    /**
     * Дата конца проведения мероприятия.
     */
    val endDate: String? = null,
    /**
     * Тип мероприятия
     */
    var type: MeetingType = MeetingType.PLANNED,
)
