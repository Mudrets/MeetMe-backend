package com.meetme.invitation.participant

import com.meetme.auth.User
import com.meetme.meeting.Meeting
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