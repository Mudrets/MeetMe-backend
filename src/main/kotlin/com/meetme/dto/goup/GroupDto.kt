package com.meetme.dto.goup

import com.meetme.dto.meeting.MeetingDto

data class GroupDto(
    val id: Long,
    val adminId: Long,
    val name: String,
    val description: String,
    val photoUrl: String?,
    val isPrivate: Boolean,
    val interests: List<String> = listOf(),
    val socialMediaLinks: Map<String, String> = mapOf()
)