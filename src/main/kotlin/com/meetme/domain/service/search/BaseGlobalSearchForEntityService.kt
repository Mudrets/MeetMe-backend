package com.meetme.domain.service.search

import com.meetme.domain.filter.FilterType

/**
 * Базовая реализация сервиса глобального поиска сущностей для определенной сущности.
 * @param Identifier тип идентификатора.
 * @param Query тип поискового запроса.
 * @param SearchedEntity тип искомой сущности.
 */
abstract class BaseGlobalSearchForEntityService<Identifier, Query, SearchedEntity> :
    GlobalSearchForEntityService<Identifier, Query, SearchedEntity> {

    /**
     * Сервис для локального посика сущностей.
     */
    abstract val searchForEntityService: SearchForEntityService<Identifier, Query, SearchedEntity>

    /**
     * Сервис для глобального поиска сущностей.
     */
    abstract val globalSearchService: GlobalSearchService<Query, SearchedEntity>

    /**
     * Поиск сущностей по переданному поисковому запросу.
     * @param identifier идентификатор сущности по которой производится локальный поиск.
     * @param query поисковой запрос.
     * @return Возвращает Map<FilterType, List<SearchedEntity>> где по ключу FilterType.MY_FILTER
     * находится результат локального поиска, а по ключу FilterType.MY_FILTER результат глобального
     * поиска.
     */
    override fun search(identifier: Identifier, query: Query): Map<FilterType, List<SearchedEntity>> {
        val searchedEntities = searchForEntityService.search(identifier, query)
        val allEntities = globalSearchService.search(query)
        val allWithoutSearchedEntities = allEntities
            .filter { entity -> !searchedEntities.contains(entity) }
        return mapOf(
            FilterType.MY_FILTER to searchedEntities,
            FilterType.GLOBAL_FILTER to allWithoutSearchedEntities,
        )
    }
}