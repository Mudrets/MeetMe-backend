package com.meetme.invitation.db

import com.meetme.group.db.Group
import com.meetme.meeting.db.Meeting
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository(value = "invitationRepository")
interface InvitationGroupToMeetingDao: JpaRepository<InvitationGroupToMeeting, Long> {

    fun findByGroupAndMeeting(group: Group, meeting: Meeting): InvitationGroupToMeeting?

    fun findAllByGroup(group: Group): List<InvitationGroupToMeeting>

    @Transactional
    fun deleteAllByMeeting(meeting: Meeting)

    @Transactional
    fun deleteAllByGroup(group: Group)
}