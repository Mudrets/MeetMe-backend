package com.meetme.domain.dto.goup

data class EditGroupDto(
    val name: String,
    val description: String,
    val photoUrl: String?,
    val isPrivate: Boolean,
    val interests: Set<String> = setOf(),
    val socialMediaLinks: Map<String, String> = mapOf()
)
