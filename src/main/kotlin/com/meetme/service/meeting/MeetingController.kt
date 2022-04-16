package com.meetme.service.meeting

import com.meetme.domain.dto.DataResponse
import com.meetme.domain.dto.meeting.CreateMeetingDto
import com.meetme.domain.dto.meeting.UpdateMeetingDto
import com.meetme.domain.dto.meeting.MeetingDto
import com.meetme.service.meeting.mapper.MeetingToMeetingDto
import com.meetme.util.tryExecute
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * Контроллер, обрабатывающий запросы для работы с мероприятиями.
 */
@RestController
@RequestMapping("/api/v1/meetings")
class MeetingController @Autowired constructor(
    /**
     * Сервис для работы с мероприятиями.
     */
    private val meetingService: MeetingService,
    /**
     * Маппер, преобразуюзий Meeting в Meeting
     */
    private val meetingToMeetingDto: MeetingToMeetingDto,
) {
    /**
     * Обработчик HTTP POST запроса по url /api/v1/meetings/create для создания мероприятия.
     * @param createMeetingDto данные для создания мероприятия.
     */
    @PostMapping("/create")
    fun createMeeting(@RequestBody createMeetingDto: CreateMeetingDto): DataResponse<MeetingDto> =
        tryExecute {
            val newMeeting = meetingService.create(createMeetingDto)
            meetingToMeetingDto(newMeeting, null)
        }

    /**
     * Обработчик HTTP GET запроса по url /api/v1/meetings/{meeting_id} для получения группы.
     * @param meetingId идентификатор мероприятия.
     */
    @GetMapping("/{meeting_id}")
    fun getMeeting(@PathVariable("meeting_id") meetingId: Long): DataResponse<MeetingDto> =
        tryExecute {
            meetingToMeetingDto(meetingService.get(meetingId), null)
        }

    /**
     * Обработчик HTTP POST запроса по url /api/v1/meetings/{meeting_id}/edit для редактиварония мероприпятия.
     * @param meetingId идентификатор мероприятия.
     * @param changes данные для изменения мероприятия.
     */
    @PostMapping("/{meeting_id}/edit")
    fun editMeeting(
        @PathVariable("meeting_id") meetingId: Long,
        @RequestBody changes: UpdateMeetingDto
    ): DataResponse<MeetingDto> =
        tryExecute {
            meetingToMeetingDto(meetingService.update(meetingId, changes), null)
        }

    /**
     * Обработчик HTTP DELETE запроса по url /api/v1/meetings//{meeting_id} для удаления мероприятия.
     * @param meetingId идентификатор мероприятия.
     */
    @DeleteMapping("/{meeting_id}")
    fun deleteMeeting(@PathVariable("meeting_id") meetingId: Long): DataResponse<Unit?> =
        tryExecute {
            meetingService.deleteByIdentifier(meetingId)
            null
        }
}