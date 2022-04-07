package com.meetme.invitation.user

import com.meetme.invitation.BaseInvitationService
import com.meetme.invitation.db.Invitation
import com.meetme.meeting.db.Meeting
import com.meetme.participants.base.ParticipantsService
import com.meetme.participants.meeting.MeetingParticipantsService
import com.meetme.user.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service("userInvitationService")
class UserInvitationServiceImpl : BaseInvitationService() {

    @Autowired
    private lateinit var userService: UserService

    @Qualifier("meetingParticipantsService")
    @Autowired
    private lateinit var meetingParticipantsService: ParticipantsService<Meeting>

    override fun addInInvitation(ids: List<Long>, invitation: Invitation): List<Long> {
        val users = userService.getListOfEntities(ids)
            .filter { user -> !user.allMeetings.contains(invitation.meeting) }
        invitation.users.addAll(users)

        return users
            .filter { user -> user == invitation.meeting.admin }
            .map { user -> user.id }
    }

    override fun addMeeting(id: Long, invitation: Invitation) {
        val user = checkUser(id, invitation)
        meetingParticipantsService.addParticipant(user.id, invitation.meeting.id)
        invitation.users.remove(user)
        userService.save(user)
    }

    override fun deleteFromInvitation(id: Long, invitation: Invitation) {
        val user = checkUser(id, invitation)
        user.invitations.remove(invitation)
        invitation.users.remove(user)
        userService.save(user)
    }

    private fun checkUser(userId: Long, invitation: Invitation) =
        invitation.users.firstOrNull { user -> user.id == userId }
            ?: throw IllegalArgumentException(
                "User with id = $userId don't have invitation on meeting with id = ${invitation.meeting.id}"
            )
}