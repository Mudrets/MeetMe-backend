package com.meetme.domain

/**
 * Создатель сущностей.
 * @param Data тип данных для создания сущености.
 * @param Entity тип сущности.
 */
interface EntityCreator<Data, Entity> {
    /**
     * Создает сущность по переданным данным.
     * @param data данные для создания сущности.
     * @return Возращает созданную сущность.
     */
    fun create(data: Data): Entity
}