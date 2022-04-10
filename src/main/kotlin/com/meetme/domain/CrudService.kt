package com.meetme.domain

interface CrudService<CreateModel, Identifier, UpdateModel, Model> {

    fun create(createModel: CreateModel): Model

    fun read(identifier: Identifier): Model

    fun update(updateModel: UpdateModel): Model

    fun delete(identifier: Identifier)

    fun save(model: Model)
}
