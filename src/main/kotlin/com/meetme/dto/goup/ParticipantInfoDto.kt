package com.meetme.dto.goup

data class ParticipantInfoDto(
    val id: Long,
    val fullName: String,
    val photoUrl: String? = null,
)
