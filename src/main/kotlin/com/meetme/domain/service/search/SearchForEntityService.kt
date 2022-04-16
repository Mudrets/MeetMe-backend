package com.meetme.domain.service.search

/**
 * Сервис поиска для определенной сущности.
 * @param Identifier тип идентификатора.
 * @param Query тип поискового запроса.
 * @param SearchedEntity тип искомой сущности.
 */
interface SearchForEntityService<Identifier, in Query, out SearchedEntity> {
    /**
     * Локальный поиск искомых сущностей по переданному поисковому запросу для
     * сущности с переданным идентификатором.
     * @param identifier идентификатор сущности.
     * @param query поисковой запрос.
     * @return Возвращает список удовлетворяющих поисковому запросу
     * искомых сущностей для сущности по переданному идентификатору.
     */
    fun search(identifier: Identifier, query: Query): List<SearchedEntity>
}