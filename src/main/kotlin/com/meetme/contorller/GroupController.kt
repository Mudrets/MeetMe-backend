package com.meetme.contorller

import com.meetme.group.dto.CreateGroupDto
import com.meetme.data.DataResponse
import com.meetme.group.Group
import com.meetme.group.GroupService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/groups")
class GroupController {

    @Autowired
    private lateinit var groupService: GroupService

    @PostMapping("/create")
    fun createGroup(@RequestBody createGroupDto: CreateGroupDto): DataResponse<Group> {
        val newMeeting = groupService.createGroup(
            adminId = createGroupDto.adminId,
            name = createGroupDto.name,
            description = createGroupDto.description,
            interests = createGroupDto.interests,
            links = createGroupDto.links,
        )

        return if (newMeeting == null)
            DataResponse(message = "User with id = ${createGroupDto.adminId} does not exist")
        else
            DataResponse(data = newMeeting)
    }
}