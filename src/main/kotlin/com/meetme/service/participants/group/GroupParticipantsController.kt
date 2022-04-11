package com.meetme.service.participants.group

import com.meetme.domain.dto.DataResponse
import com.meetme.domain.dto.auth.UserDto
import com.meetme.domain.dto.goup.GroupDto
import com.meetme.db.group.Group
import com.meetme.service.group.mapper.GroupToGroupDto
import com.meetme.service.participants.base.ParticipantsService
import com.meetme.util.tryExecute
import com.meetme.service.user.mapper.UserToUserDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/groups/{group_id}/participants")
class GroupParticipantsController {

    @Qualifier("groupParticipantsService")
    @Autowired
    private lateinit var groupParticipantsService: ParticipantsService<Group>

    @Autowired
    private lateinit var userToUserDto: UserToUserDto

    @Autowired
    private lateinit var groupToGroupDto: GroupToGroupDto

    @GetMapping
    fun getParticipants(@PathVariable("group_id") groupId: Long): DataResponse<List<UserDto>> =
        tryExecute {
            groupParticipantsService.getParticipants(groupId)
                .map(userToUserDto)
                .sortedBy(UserDto::fullName)
        }

    @PostMapping("/{user_id}")
    fun addParticipant(
        @PathVariable("group_id") groupId: Long,
        @PathVariable("user_id") userId: Long,
    ): DataResponse<GroupDto> =
        tryExecute {
            groupToGroupDto(groupParticipantsService.addParticipant(userId, groupId))
        }

    @PostMapping
    fun addParticipants(
        @PathVariable("group_id") groupId: Long,
        @RequestBody userIds: List<Long>,
    ): DataResponse<Unit?> =
        tryExecute {
            groupParticipantsService.addParticipants(userIds, groupId)
            null
        }

    @DeleteMapping("/{user_id}")
    fun removeParticipant(
        @PathVariable("group_id") groupId: Long,
        @PathVariable("user_id") userId: Long,
    ): DataResponse<GroupDto> =
        tryExecute {
            groupToGroupDto(groupParticipantsService.removeParticipant(userId, groupId))
        }
}