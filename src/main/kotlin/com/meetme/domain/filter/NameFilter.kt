package com.meetme.domain.filter

import com.meetme.domain.filter.entity.FilteredByName

interface NameFilter : (FilteredByName, String) -> Boolean

class NameFilterImpl : NameFilter {

    override fun invoke(entity: FilteredByName, searchQuery: String): Boolean =
        entity.filteredName.contains(searchQuery, true)

}