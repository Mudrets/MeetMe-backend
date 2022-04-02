package com.meetme.group

import com.meetme.domain.dto.DataResponse
import com.meetme.tryExecute
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/groups")
class InvitationController {

    @Autowired
    private lateinit var groupService: GroupService

    @PostMapping("/{group_id}/invite/{meeting_id}")
    fun inviteGroupToMeeting(
        @PathVariable("group_id") groupId: Long,
        @PathVariable("meeting_id") meetingId: Long,
    ): DataResponse<Unit?> =
        tryExecute {
            groupService.sendInvitationToGroup(groupId, meetingId)
            null
        }

    @PostMapping("/invite/{meeting_id}")
    fun invitesGroupsToMeeting(
        @PathVariable("meeting_id") meetingId: Long,
        @RequestBody groupsIds: List<Long>,
    ): DataResponse<Unit?> =
        tryExecute {
            groupService.sendInvitationToGroups(groupsIds, meetingId)
            null
        }

    @PostMapping("/{group_id}/accept/{meeting_id}")
    fun acceptInvitation(
        @PathVariable("group_id") groupId: Long,
        @PathVariable("meeting_id") meetingId: Long,
    ): DataResponse<Unit?> =
        tryExecute {
            groupService.acceptInvitation(groupId, meetingId)
            null
        }

    @PostMapping("/{group_id}/cancel/{meeting_id}")
    fun cancelInvitation(
        @PathVariable("group_id") groupId: Long,
        @PathVariable("meeting_id") meetingId: Long,
    ): DataResponse<Unit?> =
        tryExecute {
            groupService.cancelInvitation(groupId, meetingId)
            null
        }
}