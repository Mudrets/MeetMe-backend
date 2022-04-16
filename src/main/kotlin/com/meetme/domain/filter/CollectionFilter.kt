package com.meetme.domain.filter

/**
 * Фильтр коллекции по коллекции. Проверяет содержится ли
 * искомая коллекция в исходной коллекции.
 */
class CollectionFilter : Filter<Collection<*>, Collection<*>> {
    override fun invoke(interests: Collection<*>, query: Collection<*>): Boolean =
        interests.containsAll(query)
}