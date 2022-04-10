package com.meetme.domain

interface Store<Identifier, Entity> : ListEntityGetter<Identifier, Entity> {
    fun save(entity: Entity): Entity
}