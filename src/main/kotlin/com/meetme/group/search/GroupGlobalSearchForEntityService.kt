package com.meetme.group.search

import com.meetme.doIfExist
import com.meetme.domain.dto.goup.SearchGroupDto
import com.meetme.domain.filter.Filter
import com.meetme.domain.service.search.*
import com.meetme.group.GroupService
import com.meetme.group.db.Group
import com.meetme.user.UserService
import com.meetme.user.db.User
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
    private lateinit var userService: UserService

    override val globalSearchService: GlobalSearchService<SearchGroupDto, Group>
        get() = object : BaseGlobalSearchService<SearchGroupDto, Group>(groupService, filter) {

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