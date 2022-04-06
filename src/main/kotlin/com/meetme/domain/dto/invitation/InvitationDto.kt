package com.meetme.domain.dto.invitation

class InvitationDto(
    val users: List<Long> = listOf(),
    val group: List<Long> = listOf(),
)