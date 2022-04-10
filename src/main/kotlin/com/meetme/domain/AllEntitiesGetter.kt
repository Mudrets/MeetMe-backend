package com.meetme.domain

interface AllEntitiesGetter<Entity> {
    fun getAll(): List<Entity>
}