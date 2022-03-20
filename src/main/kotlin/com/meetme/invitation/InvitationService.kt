package com.meetme.invitation

import com.meetme.group.Group
import com.meetme.meeting.Meeting
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class InvitationService {

    @Autowired
    private lateinit var invitationDao: InvitationDao

    private fun getInvitation(group: Group, meeting: Meeting): Invitation =
        invitationDao.findByGroupAndMeeting(group, meeting)
            ?: throw IllegalArgumentException("Invitation for group: $group on the $meeting does not exist")

    fun sendInvitationToGroup(group: Group, meeting: Meeting): Invitation {
        if (invitationDao.findByGroupAndMeeting(group, meeting) != null)
            throw IllegalArgumentException("Invitation for group $group on the $meeting already exist")

        val newInvitation = Invitation(
            group = group,
            meeting = meeting,
        )
        invitationDao.save(newInvitation)

        return newInvitation
    }

    fun acceptInvitation(group: Group, meeting: Meeting): Invitation {
        val invitation = getInvitation(group, meeting)
        if (invitation.isCanceled)
            throw IllegalArgumentException("Invite already canceled")
        if (invitation.isAccepted)
            throw IllegalArgumentException("Invite already accepted")

        invitation.isAccepted = true
        invitationDao.save(invitation)

        return invitation
    }

    fun cancelInvitation(group: Group, meeting: Meeting): Invitation {
        val invitation = getInvitation(group, meeting)
        if (invitation.isCanceled)
            throw IllegalArgumentException("Invite already canceled")
        if (invitation.isAccepted)
            invitation.isAccepted = false

        invitation.isCanceled = true
        invitationDao.save(invitation)

        return invitation
    }
}