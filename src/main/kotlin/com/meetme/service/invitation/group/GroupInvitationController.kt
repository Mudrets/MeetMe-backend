package com.meetme.service.invitation.group

import com.meetme.domain.dto.DataResponse
import com.meetme.service.invitation.InvitationService
import com.meetme.util.tryExecute
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Контроллер, обрабатывающий запросы для работы с приглашениями групп.
 */
@RestController
@RequestMapping("/api/v1/groups")
class GroupInvitationController @Autowired constructor(
    /**
     * Сервис для работы с приглашениями.
     */
    @Qualifier("groupInvitationService")
    private val invitationService: InvitationService,
) {
    /**
     * Обработчик HTTP POST запроса по url /api/v1/groups/{group_id}/accept/{meeting_id} для принятия
     * приглашения группы на мероприятие.
     * @param groupId идентификатор группы, которая принимает приглашение.
     * @param meetingId идентификатор мероприятия, на которое было приглашена группы.
     */
    @PostMapping("/{group_id}/accept/{meeting_id}")
    fun acceptInvitation(
        @PathVariable("group_id") groupId: Long,
        @PathVariable("meeting_id") meetingId: Long,
    ): DataResponse<Unit?> =
        tryExecute {
            invitationService.acceptInvitation(groupId, meetingId)
            null
        }

    /**
     * Обработчик HTTP POST запроса по url /api/v1/groups/{group_id}/cancel/{meeting_id} для отмены
     * приглашения группы на мероприятие.
     * @param groupId идентификатор группы, которая отклоняет приглашение.
     * @param meetingId идентификатор мероприятия, на которое было приглашена группы.
     */
    @PostMapping("/{group_id}/cancel/{meeting_id}")
    fun cancelInvitation(
        @PathVariable("group_id") groupId: Long,
        @PathVariable("meeting_id") meetingId: Long,
    ): DataResponse<Unit?> =
        tryExecute {
            invitationService.cancelInvitation(groupId, meetingId)
            null
        }
}