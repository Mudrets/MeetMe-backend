package com.meetme.domain

/**
 * Поставщик сущностей.
 * @param Identifier тип идентификатора.
 * @param Entity тип сущности.
 */
interface EntityGetter<in Identifier, out Entity> {

    /**
     * Получает сущность по переданному идентификатору.
     * @param identifier идентификатор.
     * @return Возвращает полученную сущность.
     */
    fun get(identifier: Identifier): Entity

    /**
     * Получает список сущностей по списку их идентификаторов.
     * @param identifiers список идентификаторов.
     * @return Возвращает список найденных сущностей.
     */
    fun getList(identifiers: List<Identifier>): List<Entity> =
        identifiers.mapNotNull(this::get)

    /**
     * Получает все существующие сущности.
     * @return Возвращает список всех сущностей.
     */
    fun getAll(): List<Entity>
}