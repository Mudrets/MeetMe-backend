package com.meetme.service.invitation.search

import com.meetme.util.doIfExist
import com.meetme.domain.dto.meeting.SearchMeetingDto
import com.meetme.domain.service.search.SearchForEntityService
import com.meetme.db.meeting.Meeting
import com.meetme.service.user.UserServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class InvitationSearchServiceImpl : InvitationSearchService {

    @Qualifier("invitationSearchForUserService")
    @Autowired
    private lateinit var invitationSearchForUserService: SearchForEntityService<Long, SearchMeetingDto, Meeting>

    @Qualifier("invitationSearchForGroupService")
    @Autowired
    private lateinit var invitationSearchForGroupService: SearchForEntityService<Long, SearchMeetingDto, Meeting>

    @Autowired
    private lateinit var userService: UserServiceImpl

    private fun userInvitations(userId: Long, query: SearchMeetingDto) =
        invitationSearchForUserService.search(userId, query)

    private fun groupInvitations(groupId: Long, query: SearchMeetingDto) =
        invitationSearchForGroupService.search(groupId, query)

    override fun search(userId: Long, query: SearchMeetingDto): Map<String, List<Meeting>> =
        userId.doIfExist(userService) { user ->
            val nameWithIdToMeetings = mutableMapOf<String, List<Meeting>>()
            nameWithIdToMeetings[user.nameWithId] = userInvitations(userId, query)
            user.managedGroups.forEach { group ->
                nameWithIdToMeetings[group.nameWithId] = groupInvitations(group.id, query)
            }
            nameWithIdToMeetings
        }
}