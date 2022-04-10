package com.meetme.invitation.search

import com.meetme.domain.dto.meeting.SearchMeetingDto
import com.meetme.meeting.db.Meeting

interface InvitationSearchService {
    fun search(userId: Long, query: SearchMeetingDto): Map<String, List<Meeting>>
}