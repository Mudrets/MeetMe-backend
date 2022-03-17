package com.meetme.group.dto

data class CreateGroupDto(
    val adminId: Long,
    val name: String,
    val description: String = "",
    val interests: Set<String> = setOf(),
    val links: MutableMap<String, String> = mutableMapOf()
)