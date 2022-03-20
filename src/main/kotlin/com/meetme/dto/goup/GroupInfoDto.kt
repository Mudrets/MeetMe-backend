package com.meetme.dto.goup

data class GroupInfoDto(
    val id: Long,
    val name: String,
    val photoUrl: String? = null,
)
