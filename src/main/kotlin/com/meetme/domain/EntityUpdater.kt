package com.meetme.domain

/**
 * Редактор сущностей.
 * @param Data тип данных изменений сущности.
 * @param Entity тип сущности.
 * @param Identifier тип идентификатора.
 */
interface EntityUpdater<Data, Entity, Identifier> {
    /**
     * Изменяет сущность в соответствии с переданными данными.
     * @param identifier идентификатор сущности.
     * @param data данные для изменения сущности.
     * @return Возвращает измененную сущность.
     */
    fun update(identifier: Identifier, data: Data): Entity
}