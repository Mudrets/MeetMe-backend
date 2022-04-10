package com.meetme

import com.meetme.domain.Store
import org.springframework.stereotype.Service

@Service
interface StoreService<Identifier, Entity> : Store<Identifier, Entity>
