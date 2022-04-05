package com.meetme.participants.meeting

import com.meetme.domain.dto.DataResponse
import com.meetme.domain.dto.auth.UserDto
import com.meetme.domain.dto.meeting.MeetingDto
import com.meetme.meeting.db.Meeting
import com.meetme.meeting.mapper.MeetingToMeetingDto
import com.meetme.participants.base.ParticipantsService
import com.meetme.tryExecute
import com.meetme.user.mapper.UserToUserDto
import com.meetme.util.Constants.NON_EXISTENT_USER_ID
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/meetings")
class MeetingParticipantsController {

    @Qualifier("meetingParticipantsService")
    @Autowired
    private lateinit var meetingParticipantsService: ParticipantsService<Meeting>

    @Autowired
    private lateinit var userToUserDto: UserToUserDto

    @Autowired
    private lateinit var meetingToMeetingDto: MeetingToMeetingDto

    @PostMapping("/{meeting_id}/add/{user_id}")
    fun addParticipant(
        @PathVariable("meeting_id") meetingId: Long,
        @PathVariable("user_id") userId: Long,
    ): DataResponse<MeetingDto> =
        tryExecute {
            meetingToMeetingDto(
                meetingParticipantsService.addParticipant(userId, meetingId),
                userId,
            )
        }

    @PostMapping("/{meeting_id}/add")
    fun addParticipants(
        @PathVariable("meeting_id") meetingId: Long,
        @RequestBody userIds: List<Long>,
    ): DataResponse<MeetingDto> =
        tryExecute {
            meetingToMeetingDto(
                meetingParticipantsService.addParticipants(userIds, meetingId),
                NON_EXISTENT_USER_ID,
            )
        }

    @DeleteMapping("/{meeting_id}/delete/{user_id}")
    fun deleteParticipant(
        @PathVariable("meeting_id") meetingId: Long,
        @PathVariable("user_id") userId: Long,
    ): DataResponse<MeetingDto> =
        tryExecute {
            meetingToMeetingDto(
                meetingParticipantsService.removeParticipant(userId, meetingId),
                userId,
            )
        }

    @GetMapping("/{meeting_id}/participants")
    fun getParticipants(
        @PathVariable("meeting_id") meetingId: Long
    ): DataResponse<List<UserDto>> =
        tryExecute {
            meetingParticipantsService.getParticipants(meetingId)
                .map(userToUserDto)
        }
}