package com.meetme.dto.meeting

data class MeetingInfoDto(
    val id: Long,
    val name: String,
    val imageUrl: String? = null,
)