package com.meetme.group

import com.meetme.domain.CrudService
import com.meetme.domain.dto.goup.CreateGroupDto
import com.meetme.domain.dto.goup.UpdateGroupDto
import com.meetme.group.db.Group
import com.meetme.meeting.db.Meeting
import org.springframework.stereotype.Service

@Service
interface GroupService : CrudService<CreateGroupDto, UpdateGroupDto, Long, Group> {
    fun getMeetings(groupId: Long): List<Meeting>
}