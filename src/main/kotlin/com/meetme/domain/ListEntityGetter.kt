package com.meetme.domain

interface ListEntityGetter<out T> : EntityGetter<T> {

    fun getListOfEntities(ids: List<Long>): List<T> =
        ids.mapNotNull(::getEntity)
}