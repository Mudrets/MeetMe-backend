package com.meetme.domain.dto.invitation

class InvitationDto(
    val users: List<Long> = listOf(),
    val groups: List<Long> = listOf(),
)