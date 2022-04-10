package com.meetme.domain

interface EntityStore<Entity, Identifier> {
    fun save(entity: Entity): Entity

    fun delete(entity: Entity)

    fun deleteByIdentifier(identifier: Identifier)
}