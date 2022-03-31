package com.meetme.domain.dto.meeting

data class SearchQuery(
    val searchQuery: String,
    val interests: List<String>,
)
