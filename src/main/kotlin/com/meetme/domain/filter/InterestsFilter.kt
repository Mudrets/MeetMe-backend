package com.meetme.domain.filter

import com.meetme.domain.filter.entity.FilteredByInterests

interface InterestsFilter : (FilteredByInterests, Collection<String>) -> Boolean

class InterestsFilterImpl : InterestsFilter {
    override fun invoke(entity: FilteredByInterests, interests: Collection<String>): Boolean =
        entity.filteredInterests.containsAll(interests)

}