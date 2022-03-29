package com.meetme.data.dto.meeting

import java.util.*

data class MeetingDto(
    val id: Long,
    val adminId: Long?,
    val name: String,
    val description: String? = null,
    val startDate: String,
    val endDate: String? = null,
    val isPrivate: Boolean = false,
    val isOnline: Boolean = true,
    val location: String? = null,
    val maxNumberOfParticipants: Int = 1,
    val numberOfParticipants: Int = 1,
    val interests: List<String> = listOf(),
    val imageUrl: String? = null,
)