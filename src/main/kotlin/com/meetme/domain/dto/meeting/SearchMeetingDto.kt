package com.meetme.domain.dto.meeting

import com.meetme.domain.dto.meeting.enums.MeetingType

data class SearchMeetingDto(
    val searchQuery: String = "",
    val interests: List<String> = listOf(),
    val maxNumberOfParticipants: Int? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    var type: MeetingType = MeetingType.PLANNED,
)
