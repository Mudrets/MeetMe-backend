package com.meetme.dto.goup

data class GroupDto(
    val id: Long,
    val name: String,
    val photoUrl: String? = null,
)
