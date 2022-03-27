package com.meetme.invitation.group

import com.meetme.group.Group
import com.meetme.meeting.Meeting
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository(value = "invitationRepository")
interface InvitationGroupToMeetingDao: JpaRepository<InvitationGroupToMeeting, Long> {

    fun findByGroupAndMeeting(group: Group, meeting: Meeting): InvitationGroupToMeeting?
}