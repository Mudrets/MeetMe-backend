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

/**
 * Реализация сервиса глобального поиска групп для пользователя.
 */
@Service
class GroupGlobalSearchForEntityService @Autowired constructor(
    private val filter: Filter<Group, SearchGroupDto>,
    private val groupService: GroupService,
    private val userService: UserServiceImpl,
) : BaseGlobalSearchForEntityService<Long, SearchGroupDto, Group>() {

    /**
     * Сервис для глобального поиска групп.
     */
    override val globalSearchService: GlobalSearchService<SearchGroupDto, Group>
        get() = object : BaseGlobalSearchService<SearchGroupDto, Group, Long>(groupService, filter) {

            override fun prepareForResult(filteredEntities: List<Group>): List<Group> =
                filteredEntities
                    .filter { group -> !group.isPrivate }
                    .sortedBy(Group::name)
        }

    /**
     * Сервис для локального посика групп, к которым некоторый пользователь имеет отношение.
     */
    override val searchForEntityService: SearchForEntityService<Long, SearchGroupDto, Group>
        get() = object : BaseSearchForEntityService<User, Long, SearchGroupDto, Group>(filter) {

            override fun getEntityWithCheck(identifier: Long): User =
                identifier.doIfExist(userService) { it }

            override fun getSearchedEntities(entity: User): List<Group> =
                entity.groups.toList()
        }
}