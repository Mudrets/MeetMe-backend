package com.meetme.invitation.user

import com.meetme.domain.dto.DataResponse
import com.meetme.invitation.InvitationService
import com.meetme.tryExecute
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/meetings")
class UserInvitationController {

    @Qualifier("userInvitationService")
    @Autowired
    private lateinit var invitationService: InvitationService

    @PostMapping("/{meeting_id}/invite")
    fun sendInvitations(
        @PathVariable("meeting_id") meetingId: Long,
        @RequestBody userIds: List<Long>,
    ): DataResponse<Unit?> =
        tryExecute {
            invitationService.sendInvitations(userIds, meetingId)
            null
        }

    @PostMapping("/{meeting_id}/accept/{user_id}")
    fun acceptInvitation(
        @PathVariable("user_id") userId: Long,
        @PathVariable("meeting_id") meetingId: Long
    ): DataResponse<Unit?> =
        tryExecute {
            invitationService.acceptInvitation(userId, meetingId)
            null
        }

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