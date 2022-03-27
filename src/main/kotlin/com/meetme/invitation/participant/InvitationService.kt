package com.meetme.invitation.participant

import com.meetme.auth.User
import com.meetme.group.Group
import com.meetme.invitation.group.InvitationGroupToMeeting
import com.meetme.meeting.Meeting
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class InvitationService {

    @Autowired
    private lateinit var invitationDao: InvitationDao

    private fun getInvitation(user: User, meeting: Meeting): Invitation =
        invitationDao.findByUserAndMeeting(user, meeting)
            ?: throw IllegalArgumentException(
                "Invitation for user with id ${user.id} on the meeting with id ${meeting.id} does not exist"
            )

    fun sendInvitation(user: User, meeting: Meeting): Invitation {
        if (invitationDao.findByUserAndMeeting(user, meeting) != null)
            throw IllegalArgumentException("Invitation for group $user on the $meeting already exist")

        return invitationDao.save(
            Invitation(
                user = user,
                meeting = meeting,
            )
        )
    }

    fun acceptInvitation(user: User, meeting: Meeting): Invitation {
        val invitation = getInvitation(user, meeting)
        if (invitation.isCanceled)
            throw IllegalArgumentException("Invite already canceled")
        if (invitation.isAccepted)
            throw IllegalArgumentException("Invite already accepted")

        invitation.isAccepted = true
        invitationDao.save(invitation)

        return invitation
    }

    fun cancelInvitation(user: User, meeting: Meeting): Invitation {
        val invitation = getInvitation(user, meeting)
        if (invitation.isCanceled)
            throw IllegalArgumentException("Invite already canceled")
        if (invitation.isAccepted)
            invitation.isAccepted = false

        invitation.isCanceled = true
        invitationDao.save(invitation)

        return invitation
    }
}