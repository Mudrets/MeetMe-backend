package com.meetme.service.invitation.search

import com.meetme.domain.dto.meeting.SearchMeetingDto
import com.meetme.db.meeting.Meeting

/**
 * Сервис для поиска приглашений.
 */
interface InvitationSearchService {
    /**
     * Поиск мероприятий, на которые приглашен пользоваетель по
     * переданному поисковому запросу.
     * @param userId идентификатор пользователя.
     * @param query поисковой запрос.
     * @return Возвращает список найденных мероприятий.
     */
    fun search(userId: Long, query: SearchMeetingDto): Map<String, List<Meeting>>
}