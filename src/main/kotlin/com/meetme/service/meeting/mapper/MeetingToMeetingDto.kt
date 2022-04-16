package com.meetme.service.meeting.mapper

import com.meetme.db.meeting.Meeting
import com.meetme.domain.dto.meeting.MeetingDto

/**
 * Маппер, преобразующий Meeting в MeetingDto
 */
interface MeetingToMeetingDto: (Meeting, Long?) -> MeetingDto