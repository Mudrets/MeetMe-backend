package com.meetme.service.invitation.search

import com.meetme.util.doIfExist
import com.meetme.domain.dto.meeting.SearchMeetingDto
import com.meetme.domain.filter.Filter
import com.meetme.domain.service.search.BaseSearchForEntityService
import com.meetme.service.group.GroupServiceImpl
import com.meetme.db.group.Group
import com.meetme.db.invitation.Invitation
import com.meetme.db.meeting.Meeting
import com.meetme.service.group.GroupService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Реализация сервиса поиска приглашений групп.
 */
@Service("invitationSearchForGroupService")
class InvitationSearchForGroupService @Autowired constructor(
    filter: Filter<Meeting, SearchMeetingDto>,
    /**
     * Сервис для работы с группами.
     */
    private val groupService: GroupService,
) : BaseSearchForEntityService<Group, Long, SearchMeetingDto, Meeting>(filter) {

    /**
     * Находит группу по ее идентификатору.
     * @param identifier идентификатор группы.
     * @return Возвращает группу с переданным идентификатором.
     */
    override fun getEntityWithCheck(identifier: Long): Group =
        identifier.doIfExist(groupService) { it }

    /**
     * Локально получает список искомых мероприятий по переданной группе.
     * @param entity группа по которой осуществляется локальный поиск.
     * @return Возвращается список мероприятий.
     */
    override fun getSearchedEntities(entity: Group): List<Meeting> =
        entity.invitations
            .map(Invitation::meeting)

    /**
     * Подготовливает конечный список мероприятий.
     * @param filteredEntities список мероприятий.
     * @return результат поискового запроса.
     */
    override fun prepareForResult(filteredEntities: List<Meeting>): List<Meeting> =
        filteredEntities.sortedBy(Meeting::name)
}