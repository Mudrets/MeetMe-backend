package com.meetme.service.meeting.search

import com.meetme.util.doIfExist
import com.meetme.domain.service.search.BaseSearchForEntityService
import com.meetme.domain.dto.meeting.SearchMeetingDto
import com.meetme.domain.filter.Filter
import com.meetme.db.meeting.Meeting
import com.meetme.service.user.UserServiceImpl
import com.meetme.db.user.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Реализация сервиса для посика мероприятий.
 */
@Service("meetingSearchForUserService")
class MeetingSearchForUserService @Autowired constructor(
    filter: Filter<Meeting, SearchMeetingDto>,
    private val userService: UserServiceImpl,
) : BaseSearchForEntityService<User, Long, SearchMeetingDto, Meeting>(filter) {
    /**
     * Находит пользователя по его идентификатору.
     * @param identifier идентификатор пользователя.
     * @return Возвращает пользователя с переданным идентификатором.
     */
    override fun getEntityWithCheck(identifier: Long): User =
        identifier.doIfExist(userService) { it }

    /**
     * Локально получает мероприятий по переданному пользователю.
     * @param entity пользователь по которому осуществляется локальный поиск.
     * @return Возвращается список мероприятий.
     */
    override fun getSearchedEntities(entity: User): List<Meeting> =
        entity.allMeetings.toList()
}