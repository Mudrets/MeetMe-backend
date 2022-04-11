package com.meetme.service.participants.meeting

import com.meetme.service.meeting.MeetingService
import com.meetme.db.meeting.Meeting
import com.meetme.service.participants.base.ParticipantsBaseService
import com.meetme.service.user.UserService
import com.meetme.db.user.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service("meetingParticipantsService")
class MeetingParticipantsService @Autowired constructor(
    userService: UserService,
    meetingService: MeetingService,
) : ParticipantsBaseService<Meeting>(userService, meetingService) {

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