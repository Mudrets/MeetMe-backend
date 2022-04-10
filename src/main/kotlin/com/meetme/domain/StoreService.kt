package com.meetme.domain

interface StoreService<Entity, Identifier> : EntityStore<Entity, Identifier>, EntityGetter<Identifier, Entity>