package com.meetme.domain.dto.meeting

import com.meetme.domain.dto.meeting.enums.MeetingType

data class SearchMeetingDto(
    val searchQuery: String = "",
    val interests: List<String> = listOf(),
    val maxNumberOfParticipants: Int? = null,
    var type: MeetingType = MeetingType.PLANNED,
)
