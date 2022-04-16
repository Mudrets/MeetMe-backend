package com.meetme.service.invitation.user

import com.meetme.domain.dto.DataResponse
import com.meetme.service.invitation.InvitationService
import com.meetme.util.tryExecute
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.bind.annotation.*

/**
 * Обрабатывает запросы для работы с приглашениями пользователей на мероприятие.
 */
@RestController
@RequestMapping("/api/v1/meetings")
class UserInvitationController @Autowired constructor(
    /**
     * Сервис для работы с приглашениями.
     */
    @Qualifier("userInvitationService")
    private val invitationService: InvitationService
) {

    /**
     * Обработчик HTTP POST запроса по url /api/v1/meetings/{meeting_id}/accept/{user_id} для принятия
     * приглашения пользователя на мероприятие.
     * @param userId идентификатор пользователя, который принимает приглашение.
     * @param meetingId идентификатор мероприятия, на которое был приглашен пользователя.
     */
    @PostMapping("/{meeting_id}/accept/{user_id}")
    fun acceptInvitation(
        @PathVariable("user_id") userId: Long,
        @PathVariable("meeting_id") meetingId: Long
    ): DataResponse<Unit?> =
        tryExecute {
            invitationService.acceptInvitation(userId, meetingId)
            null
        }

    /**
     * Обработчик HTTP POST запроса по url /api/v1/groups/{meeting_id}/cancel/{user_id} для отмены
     * приглашения пользователя на мероприятие.
     * @param userId идентификатор пользователя, который отклоняет приглашение.
     * @param meetingId идентификатор мероприятия, на которое было приглашена группы.
     */
    @PostMapping("/{meeting_id}/cancel/{user_id}")
    fun cancelInvitation(
        @PathVariable("user_id") userId: Long,
        @PathVariable("meeting_id") meetingId: Long
    ): DataResponse<Unit?> =
        tryExecute {
            invitationService.cancelInvitation(userId, meetingId)
            null
        }

}