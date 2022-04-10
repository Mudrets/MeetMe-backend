package com.meetme.domain.service.search

interface SearchForEntityService<Identifier, in Query, out SearchedEntity> {
    fun search(identifier: Identifier, query: Query): List<SearchedEntity>
}