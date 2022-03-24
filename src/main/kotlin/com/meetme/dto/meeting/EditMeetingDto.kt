package com.meetme.dto.meeting

import java.util.*

data class EditMeetingDto(
    val name: String? = null,
    val description: String? = null,
    val startDate: Date? = null,
    val endDate: Date? = null,
    val hasEndDate: Boolean = false,
    val isOnline: Boolean = false,
    val locate: String? = null,
    val maxNumberOfParticipants: Int = 1,
)
