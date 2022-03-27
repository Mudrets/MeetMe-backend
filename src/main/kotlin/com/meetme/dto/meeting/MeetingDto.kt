package com.meetme.dto.meeting

import java.util.*

data class MeetingDto(
    val id: Long,
    val adminId: Long?,
    val name: String,
    val description: String?,
    val startDate: Date,
    val endDate: Date? = null,
    val isPrivate: Boolean = false,
    val isOnline: Boolean = true,
    val location: String? = null,
    val maxNumberOfParticipants: Int = 1,
    val numberOfParticipants: Int = 1,
    val interests: List<String> = listOf(),
    val imageUrl: String? = null,
)