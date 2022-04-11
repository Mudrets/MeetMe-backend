package com.meetme.service.group

import com.meetme.domain.CrudService
import com.meetme.domain.dto.goup.CreateGroupDto
import com.meetme.domain.dto.goup.UpdateGroupDto
import com.meetme.db.group.Group
import com.meetme.db.meeting.Meeting
import org.springframework.stereotype.Service

@Service
interface GroupService : CrudService<CreateGroupDto, UpdateGroupDto, Long, Group> {
    fun getMeetings(groupId: Long): List<Meeting>
}