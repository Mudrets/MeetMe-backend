package com.meetme.service.invitation.search

import com.meetme.util.doIfExist
import com.meetme.domain.dto.meeting.SearchMeetingDto
import com.meetme.domain.service.search.SearchForEntityService
import com.meetme.db.meeting.Meeting
import com.meetme.service.user.UserServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

/**
 * Реализация сервиса для работы с приглашениями.
 */
@Service
class InvitationSearchServiceImpl @Autowired constructor(
    /**
     * Сервис для поиска приглашений на мероприятия для пользователя.
     */
    @Qualifier("invitationSearchForUserService")
    private val invitationSearchForUserService: SearchForEntityService<Long, SearchMeetingDto, Meeting>,
    /**
     * Серсив для приглашений на мероприятия для группы.
     */
    @Qualifier("invitationSearchForGroupService")
    private val invitationSearchForGroupService: SearchForEntityService<Long, SearchMeetingDto, Meeting>,
    /**
     * Сервис для работы с пользователем.
     */
    private val userService: UserServiceImpl,
) : InvitationSearchService {
    /**
     * Ищет список приглашения пользователя с переданным идентификатором.
     * @param userId идентификатор пользователя.
     * @param query запрос для посика мероприятий.
     * @return Возвращает список найденных мероприятий.
     */
    private fun userInvitations(userId: Long, query: SearchMeetingDto) =
        invitationSearchForUserService.search(userId, query)

    /**
     * Ищет список приглашения пользователя с переданным идентификатором.
     * @param groupId идентификатор группы.
     * @param query запрос для посика мероприятий.
     * @return Возвращает список найденных мероприятий.
     */
    private fun groupInvitations(groupId: Long, query: SearchMeetingDto) =
        invitationSearchForGroupService.search(groupId, query)

    /**
     * Поиск мероприятий, на которые приглашен пользоваетель по
     * переданному поисковому запросу.
     * @param userId идентификатор пользователя.
     * @param query поисковой запрос.
     * @return Возвращает список найденных мероприятий.
     */
    override fun search(userId: Long, query: SearchMeetingDto): Map<String, List<Meeting>> =
        userId.doIfExist(userService) { user ->
            val nameWithIdToMeetings = mutableMapOf<String, List<Meeting>>()
            nameWithIdToMeetings[user.nameWithId] = userInvitations(userId, query)
            user.managedGroups.forEach { group ->
                nameWithIdToMeetings[group.nameWithId] = groupInvitations(group.id, query)
            }
            nameWithIdToMeetings
        }
}