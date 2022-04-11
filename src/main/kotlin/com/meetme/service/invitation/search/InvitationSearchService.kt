package com.meetme.service.invitation.search

import com.meetme.domain.dto.meeting.SearchMeetingDto
import com.meetme.db.meeting.Meeting

interface InvitationSearchService {
    fun search(userId: Long, query: SearchMeetingDto): Map<String, List<Meeting>>
}