package com.meetme.data.dto.goup

import com.meetme.data.dto.meeting.MeetingDto

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