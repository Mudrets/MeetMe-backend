package com.meetme.domain.service.search

import com.meetme.domain.filter.Filter

/**
 * Базовая реализация сервиса поиска для определенной сущности.
 * @param Identifier тип идентификатора.
 * @param Query тип поискового запроса.
 * @param Entity тип сущности для которой производится локальны поиск.
 * @param SearchedEntity тип искомой сущности.
 */
abstract class BaseSearchForEntityService<Entity, Identifier, Query, SearchedEntity>(
    /**
     * Фильтр.
     */
    private val filter: Filter<SearchedEntity, Query>
) : SearchForEntityService<Identifier, Query, SearchedEntity> {

    /**
     * Находит сущность по ее идентификатору.
     * @param identifier идентификатор сущности.
     * @return Возвращает сущность с переданным идентификатором.
     */
    abstract fun getEntityWithCheck(identifier: Identifier): Entity

    /**
     * Локально получает список искомых сущностей по переданной сущности.
     * @param entity сущность по которой осуществляется локальный поиск.
     * @return Возвращается список искомых сущностей.
     */
    abstract fun getSearchedEntities(entity: Entity): List<SearchedEntity>

    /**
     * Подготовливает конечный список сущностей.
     * @param filteredEntities список сущностей.
     * @return результат поискового запроса.
     */
    open fun prepareForResult(filteredEntities: List<SearchedEntity>): List<SearchedEntity> =
        filteredEntities

    /**
     * Локальный поиск искомых сущностей по переданному поисковому запросу для
     * сущности с переданным идентификатором.
     * @param identifier идентификатор сущности.
     * @param query поисковой запрос.
     * @return Возвращает список удовлетворяющих поисковому запросу
     * искомых сущностей для сущности по переданному идентификатору.
     */
    final override fun search(identifier: Identifier, query: Query): List<SearchedEntity> {
        val entity = getEntityWithCheck(identifier)
        val searchedEntities = getSearchedEntities(entity)
        val filteredEntities = searchedEntities
            .filter { searchedEntity -> filter(searchedEntity, query) }
        return prepareForResult(filteredEntities)
    }
}