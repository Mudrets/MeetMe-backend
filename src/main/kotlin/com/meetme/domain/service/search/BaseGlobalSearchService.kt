package com.meetme.domain.service.search

import com.meetme.domain.EntityGetter
import com.meetme.domain.filter.Filter

/**
 * Базовая реализация сервиса глобального поиска сущностей.
 * @param Identifier тип идентификатора.
 * @param Query тип поискового запроса.
 * @param Entity тип искомой сущности.
 */
abstract class BaseGlobalSearchService<Query, Entity, Identifier>(
    /**
     * Провайдер сущностей.
     */
    private val entityGetter: EntityGetter<Identifier, Entity>,
    /**
     * Фильтр.
     */
    private val filter: Filter<Entity, Query>,
) : GlobalSearchService<Query, Entity> {

    /**
     * Подготовливает конечный список сущностей.
     * @param filteredEntities список сущностей.
     * @return результат поискового запроса.
     */
    abstract fun prepareForResult(filteredEntities: List<Entity>): List<Entity>

    /**
     * Поиск сущностей по переданному поисковому запросу.
     * @param query поисковой запрос.
     * @return Возвращает список удовлетворяющих поисковому запросу
     * сущностей.
     */
    final override fun search(query: Query): List<Entity> {
        val allEntities = entityGetter.getAll()
        val filteredEntities = allEntities
            .filter { searchedEntity -> filter(searchedEntity, query) }
        return prepareForResult(filteredEntities)
    }
}