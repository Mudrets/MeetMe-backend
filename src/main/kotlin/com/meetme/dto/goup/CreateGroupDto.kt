package com.meetme.dto.goup

data class CreateGroupDto(
    val adminId: Long,
    val name: String,
    val description: String = "",
    val interests: Set<String> = setOf(),
    val isPrivate: Boolean = false,
    val socialMediaLinks: MutableMap<String, String> = mutableMapOf()
)