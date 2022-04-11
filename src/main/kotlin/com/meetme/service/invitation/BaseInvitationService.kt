package com.meetme.service.invitation

import com.meetme.util.doIfExist
import com.meetme.db.invitation.Invitation
import com.meetme.db.invitation.InvitationDao
import com.meetme.service.meeting.MeetingServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
abstract class BaseInvitationService : InvitationService {

    @Autowired
    private lateinit var invitationDao: InvitationDao

    @Autowired
    private lateinit var meetingService: MeetingServiceImpl

    abstract fun addInInvitation(ids: List<Long>, invitation: Invitation): List<Long>

    abstract fun addMeeting(id: Long, invitation: Invitation)

    abstract fun deleteFromInvitation(id: Long, invitation: Invitation)

    final override fun sendInvitations(ids: List<Long>, meetingId: Long) {
        meetingId.doIfExist(meetingService) { meeting ->
            synchronized(lock) {
                val invitation =
                    invitationDao.findByMeeting(meetingId) ?: Invitation(meeting = meeting)
                val instantAcceptList = addInInvitation(ids, invitation)
                invitationDao.save(invitation)
                instantAcceptList.forEach { id -> acceptInvitation(id, meetingId) }
            }
        }
    }

    final override fun acceptInvitation(id: Long, meetingId: Long) {
        val invitation = getInvitation(meetingId)
        addMeeting(id, invitation)
        invitationDao.save(invitation)
    }

    final override fun cancelInvitation(id: Long, meetingId: Long) {
        val invitation = getInvitation(meetingId)
        deleteFromInvitation(id, invitation)
        invitationDao.save(invitation)
    }

    private fun getInvitation(meetingId: Long): Invitation =
        invitationDao.findByMeeting(meetingId)
            ?: throw IllegalArgumentException("Invitation on the meeting with id $meetingId does not exist")

    companion object {
        val lock = Any()
    }
}