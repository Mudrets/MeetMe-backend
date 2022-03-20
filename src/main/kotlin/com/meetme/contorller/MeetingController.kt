package com.meetme.contorller

import com.meetme.auth.User
import com.meetme.data.DataResponse
import com.meetme.meeting.Meeting
import com.meetme.meeting.MeetingService
import com.meetme.dto.meeting.CreateMeetingDto
import com.meetme.dto.meeting.EditMeetingDto
import com.meetme.dto.meeting.MeetingInfoDto
import com.meetme.dto.user.UserInfoDto
import com.meetme.tryExecute
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/meetings")
class MeetingController {

    @Autowired
    private lateinit var meetingService: MeetingService

    @PostMapping("/create")
    fun createMeeting(@RequestBody createMeetingDto: CreateMeetingDto): DataResponse<Meeting> {
        val newMeeting = meetingService.createMeeting(
            adminId = createMeetingDto.adminId,
            name = createMeetingDto.name,
            description = createMeetingDto.description,
            interests = createMeetingDto.interests,
            links = createMeetingDto.links,
        )

        return if (newMeeting == null)
            DataResponse(message = "User with id = ${createMeetingDto.adminId} does not exist")
        else
            DataResponse(data = newMeeting)
    }

    @GetMapping("/{meeting_id}")
    fun getMeeting(@PathVariable("meeting_id") meetingId: Long): DataResponse<Meeting> =
        tryExecute { meetingService.getMeeting(meetingId) }

    @PostMapping("/{meeting_id}/edit")
    fun editMeeting(
        @PathVariable("meeting_id") meetingId: Long,
        @RequestBody changes: EditMeetingDto
    ): DataResponse<Meeting> =
        tryExecute { meetingService.editMeeting(meetingId, changes) }

    @DeleteMapping("/{meeting_id}")
    fun deleteMeeting(@PathVariable("meeting_id") meetingId: Long): DataResponse<Unit> =
        tryExecute { meetingService.deleteMeeting(meetingId) }

    @PostMapping("/{meeting_id}/add/{user_id}")
    fun addParticipant(
        @PathVariable("meeting_id") meetingId: Long,
        @PathVariable("user_id") userId: Long,
    ): DataResponse<Meeting> =
        tryExecute { meetingService.addParticipant(meetingId, userId) }

    @DeleteMapping("/{meeting_id}/delete/{user_id}")
    fun deleteParticipant(
        @PathVariable("meeting_id") meetingId: Long,
        @PathVariable("user_id") userId: Long,
    ): DataResponse<Meeting> =
        tryExecute { meetingService.deleteParticipant(meetingId, userId) }

    @GetMapping("/{user_id}/search/{search_query}")
    fun search(
        @PathVariable("user_id") userId: Long,
        @PathVariable("search_query") searchQuery: String
    ): DataResponse<List<MeetingInfoDto>> =
        tryExecute {
            meetingService.search(userId, searchQuery)
                .map(::mapMeetingToDto)
        }

    @GetMapping("/{user_id}")
    fun getAllMeetingsForUser(
        @PathVariable("user_id") userId: Long
    ): DataResponse<List<MeetingInfoDto>> =
        tryExecute {
            meetingService.getAllMeetingsForUser(userId)
                .map(::mapMeetingToDto)
        }

    @GetMapping("/{meeting_id}/participants")
    fun getParticipants(
        @PathVariable("meeting_id") meetingId: Long
    ): DataResponse<List<UserInfoDto>> =
        tryExecute {
            meetingService.getParticipants(meetingId)
                .map(::mapUserToDto)
        }

    private fun mapMeetingToDto(meeting: Meeting): MeetingInfoDto {
        return MeetingInfoDto(
            id = meeting.id,
            name = meeting.name,
            imageUrl = meeting.imageUrl,
        )
    }

    private fun mapUserToDto(user: User): UserInfoDto {
        return UserInfoDto(
            id = user.id,
            name = user.name,
            surname = user.surname,
            photoUrl = user.photoUrl,
        )
    }
}