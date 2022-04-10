package com.meetme.domain.filter

class CollectionFilter : Filter<Collection<*>, Collection<*>> {
    override fun invoke(interests: Collection<*>, query: Collection<*>): Boolean =
        interests.containsAll(query)
}