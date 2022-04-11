package com.meetme.service.invitation.search

import com.meetme.util.doIfExist
import com.meetme.domain.dto.meeting.SearchMeetingDto
import com.meetme.domain.filter.Filter
import com.meetme.domain.service.search.BaseSearchForEntityService
import com.meetme.db.invitation.Invitation
import com.meetme.db.meeting.Meeting
import com.meetme.service.user.UserServiceImpl
import com.meetme.db.user.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service("invitationSearchForUserService")
class InvitationSearchForUserService @Autowired constructor(
    filter: Filter<Meeting, SearchMeetingDto>
) : BaseSearchForEntityService<User, Long, SearchMeetingDto, Meeting>(filter) {

    @Autowired
    private lateinit var userService: UserServiceImpl

    override fun getEntityWithCheck(identifier: Long): User =
        identifier.doIfExist(userService) { it }

    override fun getSearchedEntities(entity: User): List<Meeting> =
        entity.invitations
            .map(Invitation::meeting)

    override fun prepareForResult(filteredEntities: List<Meeting>): List<Meeting> =
        filteredEntities.sortedBy(Meeting::name)
}