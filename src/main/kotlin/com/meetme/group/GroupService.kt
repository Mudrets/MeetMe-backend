package com.meetme.group

import com.meetme.StoreService
import com.meetme.domain.AllEntitiesGetter
import com.meetme.group.db.Group
import org.springframework.stereotype.Service

@Service
interface GroupService : StoreService<Long, Group>, AllEntitiesGetter<Group> {
}