package com.meetme.domain

interface EntityGetter<in Identifier, out Entity> {

    fun get(identifier: Identifier): Entity

    fun getList(identifiers: List<Identifier>): List<Entity> =
        identifiers.mapNotNull(this::get)

    fun getAll(): List<Entity>
}