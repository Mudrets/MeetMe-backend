package com.meetme.domain

interface Store<T> : ListEntityGetter<T> {
    fun save(entity: T): T
}