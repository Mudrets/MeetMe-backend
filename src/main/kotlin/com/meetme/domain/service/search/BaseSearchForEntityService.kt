package com.meetme.domain.service.search

import com.meetme.domain.filter.Filter

abstract class BaseSearchForEntityService<Entity, Identifier, Query, SearchedEntity>(
    private val filter: Filter<SearchedEntity, Query>
) : SearchForEntityService<Identifier, Query, SearchedEntity> {

    abstract fun getEntityWithCheck(identifier: Identifier): Entity

    abstract fun getSearchedEntities(entity: Entity): List<SearchedEntity>

    open fun prepareForResult(filteredEntities: List<SearchedEntity>): List<SearchedEntity> =
        filteredEntities

    final override fun search(identifier: Identifier, query: Query): List<SearchedEntity> {
        val entity = getEntityWithCheck(identifier)
        val searchedEntities = getSearchedEntities(entity)
        val filteredEntities = searchedEntities
            .filter { searchedEntity -> filter(searchedEntity, query) }
        return prepareForResult(filteredEntities)
    }
}