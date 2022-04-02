package com.meetme.invitation.db

import com.meetme.user.db.User
import com.meetme.meeting.db.Meeting
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository("UserInvitationRepository")
interface InvitationDao : JpaRepository<Invitation, Long> {
    fun findByUserAndMeeting(user: User, meeting: Meeting): Invitation?

    fun findAllByUser(user: User): List<Invitation>

    @Transactional
    fun deleteAllByMeeting(meeting: Meeting)
}