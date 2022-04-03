package com.meetme.invitation.group

import com.meetme.group.GroupService
import com.meetme.invitation.BaseInvitationService
import com.meetme.invitation.db.Invitation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service("groupInvitationService")
class GroupInvitationServiceImpl : BaseInvitationService() {

    @Autowired
    private lateinit var groupService: GroupService

    override fun addInInvitation(ids: List<Long>, invitation: Invitation): List<Long> {
        val groups = groupService.getListOfEntities(ids)
            .filter { group -> !group.meetings.contains(invitation.meeting) }
        invitation.groups.addAll(groups)
        return groups
            .filter { group -> group.admin == invitation.meeting.admin }
            .map { it.id }
    }

    override fun addMeeting(id: Long, invitation: Invitation) {
        val group = checkGroup(id, invitation)
        val usersWithoutInvitation = group.participants
            .filter { user -> !user.meetings.contains(invitation.meeting) }

        group.meetings.add(invitation.meeting)
        invitation.users.addAll(usersWithoutInvitation)
        invitation.groups.remove(group)
    }

    override fun deleteFromInvitation(id: Long, invitation: Invitation) {
        val group = checkGroup(id, invitation)
        invitation.groups.remove(group)
    }

    private fun checkGroup(groupId: Long, invitation: Invitation) =
        invitation.groups.firstOrNull { group -> group.id == groupId }
            ?: throw IllegalArgumentException(
                "Group with id = $groupId don't have invitation on meeting with id = ${invitation.meeting.id}"
            )
}