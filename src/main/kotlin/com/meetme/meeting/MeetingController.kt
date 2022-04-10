package com.meetme.meeting

import com.meetme.domain.dto.DataResponse
import com.meetme.domain.dto.meeting.CreateMeetingDto
import com.meetme.domain.dto.meeting.EditMeetingDto
import com.meetme.domain.dto.meeting.MeetingDto
import com.meetme.domain.dto.meeting.SearchMeetingDto
import com.meetme.domain.filter.FilterType
import com.meetme.meeting.db.Meeting
import com.meetme.meeting.mapper.MeetingToMeetingDto
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

    @PostMapping("/{meeting_id}/image")
    fun uploadImage(
        @RequestParam("image") image: MultipartFile,
        @PathVariable("meeting_id") meetingId: Long,
    ): DataResponse<MeetingDto> =
        tryExecute {
            meetingToMeetingDto(meetingService.uploadImage(image, meetingId), null)
        }

}