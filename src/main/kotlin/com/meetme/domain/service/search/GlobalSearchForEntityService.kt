package com.meetme.domain.service.search

import com.meetme.domain.filter.FilterType

interface GlobalSearchForEntityService<Identifier, Query, Entity> {
    fun search(identifier: Identifier, query: Query): Map<FilterType, List<Entity>>
}