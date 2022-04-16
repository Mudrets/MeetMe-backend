package com.meetme.service.participants.meeting

import com.meetme.domain.dto.DataResponse
import com.meetme.domain.dto.auth.UserDto
import com.meetme.domain.dto.meeting.MeetingDto
import com.meetme.db.meeting.Meeting
import com.meetme.service.meeting.mapper.MeetingToMeetingDto
import com.meetme.service.participants.base.ParticipantsService
import com.meetme.util.tryExecute
import com.meetme.service.user.mapper.UserToUserDto
import com.meetme.util.Constants.NON_EXISTENT_USER_ID
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.bind.annotation.*

/**
 * Контроллер, обрабатывающий запросы, связанные с участниками мероприятия.
 */
@RestController
@RequestMapping("/api/v1/meetings")
class MeetingParticipantsController @Autowired constructor(
    /**
     * Сервис для работы с участниками мероприятия.
     */
    @Qualifier("meetingParticipantsService")
    private val meetingParticipantsService: ParticipantsService<Meeting>,
    /**
     * Маппер, преобразующий User в UserDto.
     */
    private val userToUserDto: UserToUserDto,
    /**
     * Маппер, преобразующий Group в GroupDto.
     */
    private val meetingToMeetingDto: MeetingToMeetingDto,
) {
    /**
     * Обработчик HTTP POST запроса по url /api/v1/meetings/{meeting_id}/add/{user_id} для добавляния
     * нового участника в мероприятие.
     * @param meetingId идентификатор мероприятия.
     * @param userId идентификатор добавляемого пользователя.
     */
    @PostMapping("/{meeting_id}/add/{user_id}")
    fun addParticipant(
        @PathVariable("meeting_id") meetingId: Long,
        @PathVariable("user_id") userId: Long,
    ): DataResponse<MeetingDto> =
        tryExecute {
            meetingToMeetingDto(
                meetingParticipantsService.addParticipant(userId, meetingId),
                userId,
            )
        }

    /**
     * Обработчик HTTP POST запроса по url /api/v1/meetings/{meeting_id}/add для добавление
     * участников в мероприятие.
     * @param meetingId идентификатор мероприятия.
     * @param userIds список идентификаторов добавляемых пользователей.
     */
    @PostMapping("/{meeting_id}/add")
    fun addParticipants(
        @PathVariable("meeting_id") meetingId: Long,
        @RequestBody userIds: List<Long>,
    ): DataResponse<MeetingDto> =
        tryExecute {
            meetingToMeetingDto(
                meetingParticipantsService.addParticipants(userIds, meetingId),
                NON_EXISTENT_USER_ID,
            )
        }

    /**
     * Обработчик HTTP DELETE запроса по url /api/v1/meetings/{meeting_id}/delete/{user_id} для удаления
     * участника из мероприятия.
     * @param meetingId идентификатор мероприятия.
     * @param userId идентификатор добавляемого пользователя.
     */
    @DeleteMapping("/{meeting_id}/delete/{user_id}")
    fun deleteParticipant(
        @PathVariable("meeting_id") meetingId: Long,
        @PathVariable("user_id") userId: Long,
    ): DataResponse<MeetingDto> =
        tryExecute {
            meetingToMeetingDto(
                meetingParticipantsService.removeParticipant(userId, meetingId),
                userId,
            )
        }

    /**
     * Обработчик HTTP GET запроса по url /api/v1/meetings/{meeting_id}/participants для получения
     * списка участников мероприятия.
     * @param meetingId идентификатор мероприятия.
     */
    @GetMapping("/{meeting_id}/participants")
    fun getParticipants(
        @PathVariable("meeting_id") meetingId: Long
    ): DataResponse<List<UserDto>> =
        tryExecute {
            meetingParticipantsService.getParticipants(meetingId)
                .map(userToUserDto)
        }
}