package com.meetme.domain

interface CrudService<CreateData, UpdateData, Identifier, Entity>
    : EntityCreator<CreateData, Entity>, EntityUpdater<UpdateData, Entity, Identifier>,
    StoreService<Entity, Identifier>
