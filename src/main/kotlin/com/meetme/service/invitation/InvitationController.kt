package com.meetme.service.invitation

import com.meetme.domain.dto.DataResponse
import com.meetme.domain.dto.invitation.InvitationDto
import com.meetme.util.tryExecute
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.bind.annotation.*

/**
 * Контроллер, обрабатывающий запрос на отправление приглашения на мероприятия
 */
@RestController
@RequestMapping("/api/v1/meetings/{meeting_id}/invite")
class InvitationController @Autowired constructor(
    /**
     * Сервис для работы с приглашениями пользователя на мероприятия.
     */
    @Qualifier("userInvitationService")
    private val userInvitationService: InvitationService,
    /**
     * Сервис для работы с приглашениями групп на мероприятия.
     */
    @Qualifier("groupInvitationService")
    private val groupInvitationService: InvitationService,
) {

    /**
     * Обработчик HTTP POST запроса по url /api/v1/meetings/{meeting_id}/invite для отправления
     * приглашений на мероприятия.
     * @param meetingId идентификатор мероприятия.
     * @param invitationDto идентификаторы сущностей, приглашаемых на мероприятие.
     */
    @PostMapping
    fun sendInvitations(
        @PathVariable("meeting_id") meetingId: Long,
        @RequestBody invitationDto: InvitationDto,
    ): DataResponse<Unit?> =
        tryExecute {
            userInvitationService.sendInvitations(invitationDto.users, meetingId)
            groupInvitationService.sendInvitations(invitationDto.groups, meetingId)
            null
        }
}