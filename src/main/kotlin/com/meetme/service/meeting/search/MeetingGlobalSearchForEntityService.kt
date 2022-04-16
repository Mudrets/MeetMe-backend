package com.meetme.service.meeting.search

import com.meetme.domain.dto.meeting.SearchMeetingDto
import com.meetme.domain.filter.Filter
import com.meetme.domain.service.search.*
import com.meetme.service.meeting.MeetingService
import com.meetme.db.meeting.Meeting
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

/**
 * Реализация сервиса глобального поиска мероприятий для определенной сущности.
 */
@Service
class MeetingGlobalSearchForEntityService @Autowired constructor(
    /**
     * Сервис для локального посика сущностей.
     */
    @Qualifier("meetingSearchForUserService")
    override val searchForEntityService: SearchForEntityService<Long, SearchMeetingDto, Meeting>,
    /**
     * Фильтр мероприятий по поисковому запросу.
     */
    private val filter: Filter<Meeting, SearchMeetingDto>,
    /**
     * Сервис для работы с мероприятиями.
     */
    private val meetingService: MeetingService,
) : BaseGlobalSearchForEntityService<Long, SearchMeetingDto, Meeting>() {

    /**
     * Сервис для глобального поиска сущностей.
     */
    override val globalSearchService: GlobalSearchService<SearchMeetingDto, Meeting>
        get() = object : BaseGlobalSearchService<SearchMeetingDto, Meeting, Long>(meetingService, filter) {

        override fun prepareForResult(filteredEntities: List<Meeting>): List<Meeting> =
            filteredEntities
                .filter { meeting -> !meeting.isPrivate }
                .sortedBy(Meeting::name)
    }
}