package com.meetme.domain

/**
 * Cервис предоставляющий CRUD интерфейс для работы с сущностями.
 * @param CreateData тип данных для создания сущности.
 * @param UpdateData тип данных для редактирования сущности.
 * @param Identifier тип идентификатора сущности.
 * @param Entity тип сущности.
 */
interface CrudService<CreateData, UpdateData, Identifier, Entity>
    : EntityCreator<CreateData, Entity>, EntityUpdater<UpdateData, Entity, Identifier>,
    StoreService<Entity, Identifier>
