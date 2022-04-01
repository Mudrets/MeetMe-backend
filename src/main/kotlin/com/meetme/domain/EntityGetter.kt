package com.meetme.domain

interface EntityGetter<out T> {
    fun getEntity(id: Long): T?
}