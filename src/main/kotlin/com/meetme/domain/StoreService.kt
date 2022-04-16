package com.meetme.domain

/**
 * Сервис хранения сущностей.
 * @param Entity тип хранимой сущности.
 * @param Identifier тип идентификатора сущности.
 */
interface StoreService<Entity, Identifier> : EntityStore<Entity, Identifier>, EntityGetter<Identifier, Entity>