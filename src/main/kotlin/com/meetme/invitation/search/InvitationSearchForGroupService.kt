package com.meetme.invitation.search

import com.meetme.doIfExist
import com.meetme.domain.dto.meeting.SearchMeetingDto
import com.meetme.domain.filter.Filter
import com.meetme.domain.service.search.BaseSearchForEntityService
import com.meetme.group.GroupServiceImpl
import com.meetme.group.db.Group
import com.meetme.invitation.db.Invitation
import com.meetme.meeting.db.Meeting
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