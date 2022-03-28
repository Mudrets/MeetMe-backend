package com.meetme.dto.meeting

data class EditMeetingDto(
    val name: String,
    val description: String,
    val startDate: String,
    val endDate: String? = null,
    val isOnline: Boolean,
    val locate: String,
    val maxNumberOfParticipants: Int,
)
