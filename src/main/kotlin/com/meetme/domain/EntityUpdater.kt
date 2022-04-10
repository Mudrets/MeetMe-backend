package com.meetme.domain

interface EntityUpdater<Data, Entity, Identifier> {
    fun update(identifier: Identifier, data: Data): Entity
}