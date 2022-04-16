package com.meetme.service.invitation.search

import com.meetme.util.doIfExist
import com.meetme.domain.dto.meeting.SearchMeetingDto
import com.meetme.domain.filter.Filter
import com.meetme.domain.service.search.BaseSearchForEntityService
import com.meetme.db.invitation.Invitation
import com.meetme.db.meeting.Meeting
import com.meetme.service.user.UserServiceImpl
import com.meetme.db.user.User
import com.meetme.service.user.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Базовая реализация сервиса поиска для определенной сущности.
 */
@Service("invitationSearchForUserService")
class InvitationSearchForUserService @Autowired constructor(
    filter: Filter<Meeting, SearchMeetingDto>,
    /**
     * Сервис для работы с пользователями.
     */
    private val userService: UserService,
) : BaseSearchForEntityService<User, Long, SearchMeetingDto, Meeting>(filter) {
    /**
     * Находит пользоваетля по его идентификатору.
     * @param identifier идентификатор пользователя.
     * @return Возвращает пользователя с переданным идентификатором.
     */
    override fun getEntityWithCheck(identifier: Long): User =
        identifier.doIfExist(userService) { it }

    /**
     * Локально получает список искомых мерпориятий по переданному пользователю.
     * @param entity пользователь по которой осуществляется локальный поиск.
     * @return Возвращается список искомых мероприятий.
     */
    override fun getSearchedEntities(entity: User): List<Meeting> =
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