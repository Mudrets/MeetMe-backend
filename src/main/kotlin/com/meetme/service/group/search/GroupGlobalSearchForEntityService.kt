package com.meetme.service.group.search

import com.meetme.util.doIfExist
import com.meetme.domain.dto.goup.SearchGroupDto
import com.meetme.domain.filter.Filter
import com.meetme.domain.service.search.*
import com.meetme.service.group.GroupService
import com.meetme.db.group.Group
import com.meetme.service.user.UserServiceImpl
import com.meetme.db.user.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GroupGlobalSearchForEntityService :
    BaseGlobalSearchForEntityService<Long, SearchGroupDto, Group>() {

    @Autowired
    private lateinit var filter: Filter<Group, SearchGroupDto>

    @Autowired
    private lateinit var groupService: GroupService

    @Autowired
    private lateinit var userService: UserServiceImpl

    override val globalSearchService: GlobalSearchService<SearchGroupDto, Group>
        get() = object : BaseGlobalSearchService<SearchGroupDto, Group, Long>(groupService, filter) {

            override fun prepareForResult(filteredEntities: List<Group>): List<Group> =
                filteredEntities
                    .filter { group -> !group.private }
                    .sortedBy(Group::name)
        }

    override val searchForEntityService: SearchForEntityService<Long, SearchGroupDto, Group>
        get() = object : BaseSearchForEntityService<User, Long, SearchGroupDto, Group>(filter) {

            override fun getEntityWithCheck(identifier: Long): User =
                identifier.doIfExist(userService) { it }

            override fun getSearchedEntities(entity: User): List<Group> =
                entity.groups.toList()
        }
}