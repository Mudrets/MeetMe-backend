package com.meetme.domain.dto.goup

import com.meetme.domain.dto.meeting.MeetingDto

data class GroupDto(
    val id: Long,
    val adminId: Long,
    val name: String,
    val description: String,
    val photoUrl: String?,
    val isPrivate: Boolean,
    val interests: List<String> = listOf(),
)