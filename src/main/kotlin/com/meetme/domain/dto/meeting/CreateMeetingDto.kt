package com.meetme.domain.dto.meeting

data class CreateMeetingDto(
    val adminId: Long,
    val name: String,
    val description: String? = null,
    val interests: Set<String> = setOf(),
    val location: String? = null,
    val isOnline: Boolean = false,
    val isPrivate: Boolean = false,
    val startDate: String = "",
    val endDate: String? = null,
    val maxNumberParticipants: Int = 1,
)