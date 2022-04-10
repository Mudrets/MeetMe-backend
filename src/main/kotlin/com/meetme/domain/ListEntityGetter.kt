package com.meetme.domain

interface ListEntityGetter<Identifier, out Entity> : EntityGetter<Identifier, Entity> {

    fun getListOfEntities(identifiers: List<Identifier>): List<Entity> =
        identifiers.mapNotNull(::getEntity)
}