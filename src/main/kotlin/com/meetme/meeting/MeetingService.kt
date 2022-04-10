package com.meetme.meeting

import com.meetme.domain.CrudService
import com.meetme.domain.dto.meeting.CreateMeetingDto
import com.meetme.domain.dto.meeting.UpdateMeetingDto
import com.meetme.meeting.db.Meeting

interface MeetingService : CrudService<CreateMeetingDto, UpdateMeetingDto, Long, Meeting>