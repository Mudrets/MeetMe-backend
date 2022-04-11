package com.meetme.service.invitation.search

import com.meetme.util.doIfExist
import com.meetme.domain.dto.meeting.SearchMeetingDto
import com.meetme.domain.filter.Filter
import com.meetme.domain.service.search.BaseSearchForEntityService
import com.meetme.service.group.GroupServiceImpl
import com.meetme.db.group.Group
import com.meetme.db.invitation.Invitation
import com.meetme.db.meeting.Meeting
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service("invitationSearchForGroupService")
class InvitationSearchForGroupService @Autowired constructor(
    filter: Filter<Meeting, SearchMeetingDto>
) : BaseSearchForEntityService<Group, Long, SearchMeetingDto, Meeting>(filter) {

    @Autowired
    private lateinit var groupService: GroupServiceImpl

    override fun getEntityWithCheck(identifier: Long): Group =
        identifier.doIfExist(groupService) { it }

    override fun getSearchedEntities(entity: Group): List<Meeting> =
        entity.invitations
            .map(Invitation::meeting)

    override fun prepareForResult(filteredEntities: List<Meeting>): List<Meeting> =
        filteredEntities.sortedBy(Meeting::name)
}