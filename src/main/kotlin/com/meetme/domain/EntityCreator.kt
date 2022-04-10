package com.meetme.domain

interface EntityCreator<Data, Entity> {
    fun create(data: Data): Entity
}