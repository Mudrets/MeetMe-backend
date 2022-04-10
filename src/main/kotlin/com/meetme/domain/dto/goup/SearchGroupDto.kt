package com.meetme.domain.dto.goup

data class SearchGroupDto(
    val searchQuery: String = "",
    val interests: List<String> = listOf(),
)