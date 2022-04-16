package com.meetme.service.meeting

import com.meetme.domain.CrudService
import com.meetme.domain.dto.meeting.CreateMeetingDto
import com.meetme.domain.dto.meeting.UpdateMeetingDto
import com.meetme.db.meeting.Meeting

/**
 * Сервис для работы с мероприятиями.
 */
interface MeetingService : CrudService<CreateMeetingDto, UpdateMeetingDto, Long, Meeting>