package com.meetme.domain.filter

class StringFilter : Filter<String, String> {
    override fun invoke(name: String, query: String): Boolean =
        name.contains(query, true)
}