package com.meetme.domain

interface EntityGetter<in Identifier, out Entity> {
    fun getEntity(identifier: Identifier): Entity?
}