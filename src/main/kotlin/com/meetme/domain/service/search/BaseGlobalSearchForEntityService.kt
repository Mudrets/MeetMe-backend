package com.meetme.domain.service.search

import com.meetme.domain.filter.FilterType

abstract class BaseGlobalSearchForEntityService<Identifier, Query, SearchedEntity> :
    GlobalSearchForEntityService<Identifier, Query, SearchedEntity> {

    abstract val searchForEntityService: SearchForEntityService<Identifier, Query, SearchedEntity>

    abstract val globalSearchService: GlobalSearchService<Query, SearchedEntity>

    override fun search(identifier: Identifier, query: Query): Map<FilterType, List<SearchedEntity>> {
        val searchedEntities = searchForEntityService.search(identifier, query)
        val allEntities = globalSearchService.search(query)
        val allWithoutSearchedEntities = allEntities
            .filter { entity -> !searchedEntities.contains(entity) }
        return mapOf(
            FilterType.MY_FILTER to searchedEntities,
            FilterType.GLOBAL_FILTER to allWithoutSearchedEntities,
        )
    }
}