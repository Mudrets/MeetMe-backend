package com.meetme.domain.service.search

interface GlobalSearchService<Query, Entity> {
    fun search(query: Query): List<Entity>
}
