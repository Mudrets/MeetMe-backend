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

@RestController
@RequestMapping("/api/v1/groups")
class GroupInvitationController {

    @Qualifier("groupInvitationService")
    @Autowired
    private lateinit var invitationService: InvitationService

    @PostMapping("/{group_id}/accept/{meeting_id}")
    fun acceptInvitation(
        @PathVariable("group_id") groupId: Long,
        @PathVariable("meeting_id") meetingId: Long,
    ): DataResponse<Unit?> =
        tryExecute {
            invitationService.acceptInvitation(groupId, meetingId)
            null
        }

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