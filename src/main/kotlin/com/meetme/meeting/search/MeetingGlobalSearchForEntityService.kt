package com.meetme.meeting.search

import com.meetme.doIfExist
import com.meetme.domain.dto.meeting.SearchMeetingDto
import com.meetme.domain.filter.Filter
import com.meetme.domain.service.search.*
import com.meetme.meeting.MeetingService
import com.meetme.meeting.db.Meeting
import com.meetme.user.UserService
import com.meetme.user.db.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class MeetingGlobalSearchForEntityService @Autowired constructor(
    @Qualifier("meetingSearchForUserService")
    override val searchForEntityService: SearchForEntityService<Long, SearchMeetingDto, Meeting>
):
    BaseGlobalSearchForEntityService<Long, SearchMeetingDto, Meeting>() {

    @Autowired
    private lateinit var filter: Filter<Meeting, SearchMeetingDto>

    @Autowired
    private lateinit var meetingService: MeetingService


    override val globalSearchService: GlobalSearchService<SearchMeetingDto, Meeting>
        get() = object : BaseGlobalSearchService<SearchMeetingDto, Meeting>(meetingService, filter) {

        override fun prepareForResult(filteredEntities: List<Meeting>): List<Meeting> =
            filteredEntities
                .filter { meeting -> !meeting.private }
                .sortedBy(Meeting::name)
    }
}