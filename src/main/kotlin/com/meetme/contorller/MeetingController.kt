package com.meetme.contorller

import com.meetme.domain.dto.DataResponse
import com.meetme.domain.dto.auth.UserDto
import com.meetme.services.meeting.MeetingService
import com.meetme.domain.dto.meeting.CreateMeetingDto
import com.meetme.domain.dto.meeting.EditMeetingDto
import com.meetme.domain.dto.meeting.MeetingDto
import com.meetme.domain.dto.meeting.SearchQuery
import com.meetme.domain.filter.InterestsFilter
import com.meetme.domain.filter.NameFilter
import com.meetme.domain.filter.FilterType
import com.meetme.mapper.MeetingToMeetingDto
import com.meetme.mapper.UserToUserDto
import com.meetme.tryExecute
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/meetings")
class MeetingController {

    @Autowired
    private lateinit var meetingService: MeetingService

    @Autowired
    private lateinit var meetingToMeetingDto: MeetingToMeetingDto

    @Autowired
    private lateinit var userToUserDto: UserToUserDto

    @Autowired
    private lateinit var nameFilter: NameFilter

    @Autowired
    private lateinit var interestsFilter: InterestsFilter

    @PostMapping("/create")
    fun createMeeting(@RequestBody createMeetingDto: CreateMeetingDto): DataResponse<MeetingDto> =
        tryExecute {
            val newMeeting = meetingService.createMeeting(createMeetingDto)
            meetingToMeetingDto(newMeeting)
        }

    @GetMapping("/{meeting_id}")
    fun getMeeting(@PathVariable("meeting_id") meetingId: Long): DataResponse<MeetingDto> =
        tryExecute {
            meetingToMeetingDto(meetingService.getMeeting(meetingId))
        }

    @PostMapping("/{meeting_id}/edit")
    fun editMeeting(
        @PathVariable("meeting_id") meetingId: Long,
        @RequestBody changes: EditMeetingDto
    ): DataResponse<MeetingDto> =
        tryExecute {
            meetingToMeetingDto(meetingService.editMeeting(meetingId, changes))
        }

    @DeleteMapping("/{meeting_id}")
    fun deleteMeeting(@PathVariable("meeting_id") meetingId: Long): DataResponse<Unit?> =
        tryExecute {
            meetingService.deleteMeeting(meetingId)
            null
        }

    @PostMapping("/{meeting_id}/add/{user_id}")
    fun addParticipant(
        @PathVariable("meeting_id") meetingId: Long,
        @PathVariable("user_id") userId: Long,
    ): DataResponse<MeetingDto> =
        tryExecute {
            meetingToMeetingDto(meetingService.addParticipant(meetingId, userId))
        }

    @DeleteMapping("/{meeting_id}/delete/{user_id}")
    fun deleteParticipant(
        @PathVariable("meeting_id") meetingId: Long,
        @PathVariable("user_id") userId: Long,
    ): DataResponse<MeetingDto> =
        tryExecute {
            meetingToMeetingDto(meetingService.deleteParticipant(meetingId, userId))
        }

    @GetMapping("/{user_id}/planned/search")
    fun searchPlanned(
        @PathVariable("user_id") userId: Long,
        @RequestBody searchQuery: SearchQuery
    ): DataResponse<Map<String, List<MeetingDto>>> =
        tryExecute {
            val map = meetingService.searchPlanned(userId, searchQuery)
            mapOf(
                FilterType.MY_FILTER.typeName to map[FilterType.MY_FILTER]!!.map(meetingToMeetingDto),
                FilterType.GLOBAL_FILTER.typeName to map[FilterType.GLOBAL_FILTER]!!.map(meetingToMeetingDto),
            )
        }

    @GetMapping("/{user_id}/visited/search")
    fun searchVisited(
        @PathVariable("user_id") userId: Long,
        @RequestBody searchQuery: SearchQuery,
    ): DataResponse<List<MeetingDto>> =
        tryExecute {
            meetingService.searchVisited(userId, searchQuery)
                .map(meetingToMeetingDto)
        }

    @GetMapping("/{user_id}/invites/search")
    fun searchInvites(
        @PathVariable("user_id") userId: Long,
        @RequestBody searchQuery: SearchQuery,
    ): DataResponse<Map<String, List<MeetingDto>>> =
        tryExecute {
            meetingService.searchInvites(userId, searchQuery)
                .map { (name, meetings) ->
                    name to meetings.map(meetingToMeetingDto)
                }
                .toMap()
        }

    @GetMapping("/{meeting_id}/participants")
    fun getParticipants(
        @PathVariable("meeting_id") meetingId: Long
    ): DataResponse<List<UserDto>> =
        tryExecute {
            meetingService.getParticipants(meetingId)
                .map(userToUserDto)
        }

    @PostMapping("/{meeting_id}/invite/{user_id}")
    fun sendInvitation(
        @PathVariable("user_id") userId: Long,
        @PathVariable("meeting_id") meetingId: Long
    ): DataResponse<Unit?> =
        tryExecute {
            meetingService.sendInvitation(userId, meetingId)
            null
        }

    @PostMapping("/{meeting_id}/invite")
    fun sendInvitations(
        @PathVariable("meeting_id") meetingId: Long,
        @RequestBody userIds: List<Long>,
    ): DataResponse<Unit?> =
        tryExecute {
            meetingService.sendInvitationToUsers(userIds, meetingId)
            null
        }

    @PostMapping("/{meeting_id}/accept/{user_id}")
    fun acceptInvitation(
        @PathVariable("user_id") userId: Long,
        @PathVariable("meeting_id") meetingId: Long
    ): DataResponse<Unit?> =
        tryExecute {
            meetingService.acceptInvitation(userId, meetingId)
            null
        }

    @PostMapping("/{meeting_id}/cancel/{user_id}")
    fun cancelInvitation(
        @PathVariable("user_id") userId: Long,
        @PathVariable("meeting_id") meetingId: Long
    ): DataResponse<Unit?> =
        tryExecute {
            meetingService.cancelInvitation(userId, meetingId)
            null
        }

    @GetMapping("/{user_id}/planned")
    fun getPlannedMeetings(@PathVariable("user_id") userId: Long): DataResponse<List<MeetingDto>> =
        tryExecute {
            meetingService.getPlannedMeetingsForUser(userId)
                .map(meetingToMeetingDto)
        }

    @GetMapping("/{user_id}/visited")
    fun getVisitedMeetings(@PathVariable("user_id") userId: Long): DataResponse<List<MeetingDto>> =
        tryExecute {
            meetingService.getVisitedMeetingForUser(userId)
                .map(meetingToMeetingDto)
        }

    @GetMapping("/{user_id}/invites")
    fun getInvitesOnMeetings(@PathVariable("user_id") userId: Long): DataResponse<Map<String, List<MeetingDto>>> =
        tryExecute {
            meetingService.getInvitesOnMeetings(userId)
                .map { (name, meetings) ->
                    name to meetings.map(meetingToMeetingDto)
                }
                .toMap()
        }

    @PostMapping("/{meeting_id}/image")
    fun uploadImage(
        @RequestParam("image") image: MultipartFile,
        @PathVariable("meeting_id") meetingId: Long,
    ): DataResponse<MeetingDto> =
        tryExecute {
            meetingToMeetingDto(meetingService.uploadImage(image, meetingId))
        }
}