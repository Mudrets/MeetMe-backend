package com.meetme.domain.service.search

/**
 * Сервис глобального поиска сущностей.
 * @param Query тип поискового запроса.
 * @param Entity тип искомой сущности.
 */
interface GlobalSearchService<Query, Entity> {
    /**
     * Поиск сущностей по переданному поисковому запросу.
     * @param query поисковой запрос.
     * @return Возвращает список удовлетворяющих поисковому запросу
     * сущностей.
     */
    fun search(query: Query): List<Entity>
}
