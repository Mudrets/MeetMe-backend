package com.meetme.group

import com.meetme.domain.dto.DataResponse
import com.meetme.domain.dto.auth.UserDto
import com.meetme.domain.dto.goup.GroupDto
import com.meetme.group.mapper.GroupToGroupDto
import com.meetme.user.mapper.UserToUserDto
import com.meetme.tryExecute
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/groups/{group_id}/participants")
class ParticipantsController {

    @Autowired
    private lateinit var groupService: GroupService

    @Autowired
    private lateinit var userToUserDto: UserToUserDto

    @Autowired
    private lateinit var groupToGroupDto: GroupToGroupDto

    @GetMapping
    fun getParticipants(@PathVariable("group_id") groupId: Long): DataResponse<List<UserDto>> =
        tryExecute {
            groupService.getParticipants(groupId)
                .map(userToUserDto)
                .sortedBy(UserDto::fullName)
        }

    @PostMapping
    fun addParticipant(
        @PathVariable("group_id") groupId: Long,
        @RequestBody userIds: List<Long>,
    ): DataResponse<Unit?> =
        tryExecute {
            groupService.addParticipantsToGroup(groupId, userIds)
            null
        }

    @PostMapping("/{user_id}")
    fun addParticipant(
        @PathVariable("group_id") groupId: Long,
        @PathVariable("user_id") userId: Long,
    ): DataResponse<Unit?> =
        tryExecute {
            groupService.addParticipantToGroup(groupId, userId)
            null
        }

    @DeleteMapping("/{user_id}")
    fun deleteUser(
        @PathVariable("group_id") groupId: Long,
        @PathVariable("user_id") userId: Long,
    ): DataResponse<GroupDto> =
        tryExecute {
            groupToGroupDto(groupService.deleteUser(groupId, userId))
        }
}