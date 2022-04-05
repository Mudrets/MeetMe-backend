package com.meetme.meeting

import com.meetme.domain.dto.DataResponse
import com.meetme.domain.dto.auth.UserDto
import com.meetme.domain.dto.meeting.CreateMeetingDto
import com.meetme.domain.dto.meeting.EditMeetingDto
import com.meetme.domain.dto.meeting.MeetingDto
import com.meetme.domain.dto.meeting.SearchQuery
import com.meetme.domain.filter.InterestsFilter
import com.meetme.domain.filter.NameFilter
import com.meetme.domain.filter.FilterType
import com.meetme.meeting.db.Meeting
import com.meetme.meeting.mapper.MeetingToMeetingDto
import com.meetme.user.mapper.UserToUserDto
import com.meetme.tryExecute
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/meetings")
class MeetingController {

    @Autowired
    private lateinit var meetingService: MeetingServiceImpl

    @Autowired
    private lateinit var meetingToMeetingDto: MeetingToMeetingDto

    private fun Collection<Meeting>.toMeetingDto(userId: Long): List<MeetingDto> =
        this.map { meeting -> meetingToMeetingDto(meeting, userId) }

    @PostMapping("/create")
    fun createMeeting(@RequestBody createMeetingDto: CreateMeetingDto): DataResponse<MeetingDto> =
        tryExecute {
            val newMeeting = meetingService.createMeeting(createMeetingDto)
            meetingToMeetingDto(newMeeting, null)
        }

    @GetMapping("/{meeting_id}")
    fun getMeeting(@PathVariable("meeting_id") meetingId: Long): DataResponse<MeetingDto> =
        tryExecute {
            meetingToMeetingDto(meetingService.getMeeting(meetingId), null)
        }

    @PostMapping("/{meeting_id}/edit")
    fun editMeeting(
        @PathVariable("meeting_id") meetingId: Long,
        @RequestBody changes: EditMeetingDto
    ): DataResponse<MeetingDto> =
        tryExecute {
            meetingToMeetingDto(meetingService.editMeeting(meetingId, changes), null)
        }

    @DeleteMapping("/{meeting_id}")
    fun deleteMeeting(@PathVariable("meeting_id") meetingId: Long): DataResponse<Unit?> =
        tryExecute {
            meetingService.deleteMeeting(meetingId)
            null
        }

    @PostMapping("/{user_id}/planned/search")
    fun searchPlanned(
        @PathVariable("user_id") userId: Long,
        @RequestBody searchQuery: SearchQuery
    ): DataResponse<Map<String, List<MeetingDto>>> =
        tryExecute {
            val map = meetingService.searchPlanned(userId, searchQuery)
            mapOf(
                FilterType.MY_FILTER.typeName to map[FilterType.MY_FILTER]!!.toMeetingDto(userId),
                FilterType.GLOBAL_FILTER.typeName to map[FilterType.GLOBAL_FILTER]!!.toMeetingDto(userId),
            )
        }

    @GetMapping("/{user_id}/visited/search")
    fun searchVisited(
        @PathVariable("user_id") userId: Long,
        @RequestBody searchQuery: SearchQuery,
    ): DataResponse<List<MeetingDto>> =
        tryExecute {
            meetingService.searchVisited(userId, searchQuery)
                .toMeetingDto(userId)
        }

    @GetMapping("/{user_id}/invites/search")
    fun searchInvites(
        @PathVariable("user_id") userId: Long,
        @RequestBody searchQuery: SearchQuery,
    ): DataResponse<Map<String, List<MeetingDto>>> =
        tryExecute {
            meetingService.searchInvitations(userId, searchQuery)
        }

    @GetMapping("/{user_id}/planned")
    fun getPlannedMeetings(@PathVariable("user_id") userId: Long): DataResponse<List<MeetingDto>> =
        tryExecute {
            meetingService.getPlannedMeetingsForUser(userId)
                .toMeetingDto(userId)
        }

    @GetMapping("/{user_id}/visited")
    fun getVisitedMeetings(@PathVariable("user_id") userId: Long): DataResponse<List<MeetingDto>> =
        tryExecute {
            meetingService.getVisitedMeetingForUser(userId)
                .toMeetingDto(userId)
        }

    @GetMapping("/{user_id}/invites")
    fun getInvitesOnMeetings(@PathVariable("user_id") userId: Long): DataResponse<Map<String, List<MeetingDto>>> =
        tryExecute {
            meetingService.searchInvitations(userId, SearchQuery())
        }

    @PostMapping("/{meeting_id}/image")
    fun uploadImage(
        @RequestParam("image") image: MultipartFile,
        @PathVariable("meeting_id") meetingId: Long,
    ): DataResponse<MeetingDto> =
        tryExecute {
            meetingToMeetingDto(meetingService.uploadImage(image, meetingId), null)
        }

}