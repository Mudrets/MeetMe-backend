package com.meetme.domain.service.search

import com.meetme.domain.filter.FilterType

/**
 * Сервис глобального поиска сущностей для определенной сущности.
 * @param Identifier тип идентификатора.
 * @param Query тип поискового запроса.
 * @param Entity тип искомой сущности.
 */
interface GlobalSearchForEntityService<Identifier, Query, Entity> {
    /**
     * Поиск сущностей по переданному поисковому запросу.
     * @param identifier идентификатор сущности по которой производится локальный поиск.
     * @param query поисковой запрос.
     * @return Возвращает Map<FilterType, List<SearchedEntity>> где по ключу FilterType.MY_FILTER
     * находится результат локального поиска, а по ключу FilterType.MY_FILTER результат глобального
     * поиска.
     */
    fun search(identifier: Identifier, query: Query): Map<FilterType, List<Entity>>
}