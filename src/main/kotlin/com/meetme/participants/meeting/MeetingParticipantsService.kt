package com.meetme.participants.meeting

import com.meetme.meeting.MeetingService
import com.meetme.meeting.db.Meeting
import com.meetme.participants.base.ParticipantsBaseService
import com.meetme.user.UserService
import com.meetme.user.db.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service("meetingParticipantsService")
class MeetingParticipantsService : ParticipantsBaseService<Meeting>(
    "Meeting with id = %s does not exist"
) {

    @Autowired
    private lateinit var meetingService: MeetingService

    override fun initService() {
        service = meetingService
    }

    override fun checkEntityBeforeAdd(entity: Meeting, user: User) {
        if (entity.numberOfParticipants >= entity.maxNumberOfParticipants)
            throw IllegalArgumentException("The maximum number of participants in the meeting has been exceeded")
        if (entity.participants.contains(user))
            throw IllegalArgumentException(
                "User with id = ${user.id} already is participant of meeting with id = ${entity.id}"
            )
    }

    override fun checkEntityBeforeRemove(entity: Meeting, user: User) {
        if (!entity.participants.contains(user))
            throw IllegalArgumentException(
                "The user with id = ${user.id} is not a member of the meeting ${entity.id}"
            )
    }

    override fun addContainerToUser(container: Meeting, user: User) {
        user.meetings.add(container)
    }

    override fun removeContainerFromUser(container: Meeting, user: User) {
        user.meetings.remove(container)

    }
}