package com.meetme.meeting.mapper

import com.meetme.domain.dto.meeting.MeetingDto
import com.meetme.interest.mapper.InterestsToStrings
import com.meetme.meeting.db.Meeting

interface MeetingToMeetingDto: (Meeting, Long?) -> MeetingDto

class MeetingToMeetingDtoImpl(
    private val interestsToStrings: InterestsToStrings
): MeetingToMeetingDto {

    override fun invoke(meeting: Meeting, userId: Long?): MeetingDto =
        MeetingDto(
            id = meeting.id,
            adminId = meeting.admin.id,
            name = meeting.name,
            description = meeting.description,
            startDate = meeting.startDate,
            endDate = meeting.endDate,
            isPrivate = meeting.private,
            isOnline = meeting.isOnline,
            location = meeting.location,
            maxNumberOfParticipants = meeting.maxNumberOfParticipants,
            numberOfParticipants = meeting.numberOfParticipants,
            interests = interestsToStrings(meeting.interests),
            imageUrl = meeting.photoUrl,
            isParticipant = userId?.let { meeting.participants.any { user -> user.id == it } }
        )

}