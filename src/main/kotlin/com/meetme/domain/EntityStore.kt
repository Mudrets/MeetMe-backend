package com.meetme.domain

/**
 * Хранилище сущностей.
 * @param Entity тип сущности.
 * @param Identifier тип идентификатора.
 */
interface EntityStore<Entity, Identifier> {
    /**
     * Сохроняет сущность в хранилище.
     * @param entity сохраняемая сущность.
     * @return Возвращает сохраненную сущность.
     */
    fun save(entity: Entity): Entity

    /**
     * Удаляет переданную сущность.
     * @param entity удаляемая сущность.
     */
    fun delete(entity: Entity)

    /**
     * Удаляет сущность по переданному идентификатору.
     * @param identifier идентификатор удаляемой сущности.
     */
    fun deleteByIdentifier(identifier: Identifier)
}