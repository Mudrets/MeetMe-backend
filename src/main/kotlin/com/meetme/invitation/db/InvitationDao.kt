package com.meetme.invitation.db

import com.meetme.group.db.Group
import com.meetme.user.db.User
import com.meetme.meeting.db.Meeting
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository("UserInvitationRepository")
interface InvitationDao : JpaRepository<Invitation, Long> {
    @Query("SELECT inv FROM Invitation inv WHERE inv.meeting.id = ?1")
    fun findByMeeting(meetingId: Long): Invitation?

    @Transactional
    fun deleteAllByMeeting(meeting: Meeting)
}