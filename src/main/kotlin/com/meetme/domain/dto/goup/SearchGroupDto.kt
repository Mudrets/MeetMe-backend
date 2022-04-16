package com.meetme.domain.dto.goup

/**
 * Data transfer object поиска группы.
 */
data class SearchGroupDto(
    /**
     * Поисковой запрос.
     */
    val searchQuery: String = "",
    /**
     * Требуемые интересы.
     */
    val interests: List<String> = listOf(),
)