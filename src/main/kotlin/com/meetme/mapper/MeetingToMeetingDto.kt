package com.meetme.mapper

import com.meetme.dto.meeting.MeetingDto
import com.meetme.meeting.Meeting
import org.springframework.beans.factory.annotation.Autowired

interface MeetingToMeetingDto: (Meeting) -> MeetingDto

class MeetingToMeetingDtoImpl(
    private val interestsToStrings: InterestsToStrings
): MeetingToMeetingDto {

    override fun invoke(meeting: Meeting): MeetingDto =
        MeetingDto(
            id = meeting.id,
            adminId = meeting.admin?.id,
            name = meeting.name,
            description = meeting.description,
            startDate = meeting.startDate,
            endDate = meeting.endDate,
            isPrivate = meeting.private,
            isOnline = meeting.isOnline,
            location = meeting.location,
            maxNumberOfParticipants = meeting.maxNumberOfParticipants,
            numberOfParticipants = meeting.numberOfParticipants,
            interests = interestsToStrings(meeting.interests.toList())
        )

}