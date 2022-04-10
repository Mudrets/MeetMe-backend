package com.meetme.group

import com.meetme.domain.dto.goup.CreateGroupDto
import com.meetme.domain.dto.DataResponse
import com.meetme.domain.dto.goup.UpdateGroupDto
import com.meetme.domain.dto.goup.GroupDto
import com.meetme.domain.dto.meeting.MeetingDto
import com.meetme.group.mapper.GroupToGroupDto
import com.meetme.meeting.mapper.MeetingToMeetingDto
import com.meetme.tryExecute
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/groups")
class GroupController {

    @Autowired
    private lateinit var groupService: GroupServiceImpl

    @Autowired
    private lateinit var groupToGroupDto: GroupToGroupDto

    @Autowired
    private lateinit var meetingToMeetingDto: MeetingToMeetingDto


    @PostMapping("/create")
    fun createGroup(@RequestBody createGroupDto: CreateGroupDto): DataResponse<GroupDto> =
        tryExecute {
            groupToGroupDto(groupService.create(createGroupDto))
        }

    @PostMapping("/{group_id}/edit")
    fun editGroup(
        @PathVariable("group_id") groupId: Long,
        @RequestBody editCredentials: UpdateGroupDto,
    ): DataResponse<GroupDto> =
        tryExecute {
            groupToGroupDto(groupService.update(groupId, editCredentials))
        }

    @GetMapping("/{group_id}")
    fun getGroup(@PathVariable("group_id") groupId: Long): DataResponse<GroupDto> =
        tryExecute {
            groupToGroupDto(groupService.getGroup(groupId))
        }

    @DeleteMapping("/{group_id}/{user_id}")
    fun deleteGroup(
        @PathVariable("group_id") groupId: Long,
        @PathVariable("user_id") userId: Long,
    ): DataResponse<Unit?> =
        tryExecute {
            groupService.deleteByIdentifier(groupId)
            null
        }

    @GetMapping("/{group_id}/meetings")
    fun getMeetingsOfGroup(@PathVariable("group_id") groupId: Long): DataResponse<List<MeetingDto>> =
        tryExecute {
            groupService.getMeetings(groupId)
                .map { meeting -> meetingToMeetingDto(meeting, null) }
        }
}