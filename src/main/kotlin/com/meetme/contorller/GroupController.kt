package com.meetme.contorller

import com.meetme.dto.goup.CreateGroupDto
import com.meetme.data.DataResponse
import com.meetme.dto.goup.GroupInfoDto
import com.meetme.dto.goup.ParticipantInfoDto
import com.meetme.group.Group
import com.meetme.group.GroupService
import com.meetme.invitation.group.InvitationGroupToMeeting
import com.meetme.tryExecute
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/groups")
class GroupController {

    @Autowired
    private lateinit var groupService: GroupService

    @PostMapping("/create")
    fun createGroup(@RequestBody createGroupDto: CreateGroupDto): DataResponse<Group> =
        tryExecute {
            val newGroup = groupService.createGroup(
                adminId = createGroupDto.adminId,
                name = createGroupDto.name,
                description = createGroupDto.description,
                interests = createGroupDto.interests,
                links = createGroupDto.links,

            )
            newGroup
        }

    @PostMapping("/{group_id}/participant/{user_id}")
    fun addUser(
        @PathVariable("group_id") groupId: Long,
        @PathVariable("user_id") userId: Long,
    ): DataResponse<Group> =
        tryExecute { groupService.addParticipantToGroup(groupId, userId) }

    @DeleteMapping("/{group_id}/participant/{user_id}")
    fun deleteUser(
        @PathVariable("group_id") groupId: Long,
        @PathVariable("user_id") userId: Long,
    ): DataResponse<Group> =
        tryExecute { groupService.deleteUser(groupId, userId) }

    @GetMapping("/{group_id}")
    fun getGroup(@PathVariable("group_id") groupId: Long): DataResponse<Group> =
        tryExecute { groupService.getGroup(groupId) }

    @GetMapping("/{group_id}/participants")
    fun getParticipantsOfGroup(@PathVariable("group_id") groupId: Long): DataResponse<List<ParticipantInfoDto>> =
        tryExecute {
            groupService.getParticipants(groupId)
                .map { user ->
                    ParticipantInfoDto(
                        id = user.id,
                        fullName = "${user.name} ${user.surname}",
                        photoUrl = null,
                    )
                }
                .sortedBy(ParticipantInfoDto::fullName)
        }

    @DeleteMapping("/{group_id}/{user_id}")
    fun deleteGroup(
        @PathVariable("group_id") groupId: Long,
        @PathVariable("user_id") userId: Long,
    ): DataResponse<Unit> =
        tryExecute {
            groupService.deleteGroup(groupId, userId)
        }

    @PostMapping("/{group_id}/accept/{meeting_id}")
    fun acceptInvitation(
        @PathVariable("group_id") groupId: Long,
        @PathVariable("meeting_id") meetingId: Long,
    ): DataResponse<InvitationGroupToMeeting> =
        tryExecute {
            groupService.acceptInvitation(groupId, meetingId)
        }

    @PostMapping("/{group_id}/cancel/{meeting_id}")
    fun cancelInvitation(
        @PathVariable("group_id") groupId: Long,
        @PathVariable("meeting_id") meetingId: Long,
    ): DataResponse<InvitationGroupToMeeting> =
        tryExecute {
            groupService.cancelInvitation(groupId, meetingId)
        }

    @PostMapping("/{group_id}/invite/{meeting_id}")
    fun inviteGroupToMeeting(
        @PathVariable("group_id") groupId: Long,
        @PathVariable("meeting_id") meetingId: Long,
    ): DataResponse<InvitationGroupToMeeting> =
        tryExecute {
            groupService.sendInvitationToGroup(groupId, meetingId)
        }

    @GetMapping("/{group_id}/{search_query}")
    fun searchGroup(
        @PathVariable("group_id") groupId: Long,
        @PathVariable("search_query") searchQuery: String,
    ): DataResponse<List<GroupInfoDto>> =
        tryExecute {
            groupService.searchGroups(searchQuery)
                .map { group ->
                    GroupInfoDto(
                        id = group.id,
                        name = group.name,
                        photoUrl = group.photoUrl,
                    )
                }
        }
}