package com.meetme.invitation

import com.meetme.group.Group
import com.meetme.meeting.Meeting
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository(value = "invitationRepository")
interface InvitationDao: JpaRepository<Invitation, Long> {

    fun findByGroupAndMeeting(group: Group, meeting: Meeting): Invitation?
}