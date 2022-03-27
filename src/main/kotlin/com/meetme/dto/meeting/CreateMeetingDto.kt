package com.meetme.dto.meeting

import java.time.Instant
import java.util.*

data class CreateMeetingDto(
    val adminId: Long,
    val name: String,
    val description: String = "",
    val interests: Set<String> = setOf(),
    val location: String? = null,
    val isOnline: Boolean = false,
    val isPrivate: Boolean = false,
    val startDate: Date = Date.from(Instant.now()),
    val endDate: Date? = null,
    val maxNumberParticipants: Int = 1,
)