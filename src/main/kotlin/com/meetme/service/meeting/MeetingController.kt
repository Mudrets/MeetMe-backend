package com.meetme.service.meeting

import com.meetme.domain.dto.DataResponse
import com.meetme.domain.dto.meeting.CreateMeetingDto
import com.meetme.domain.dto.meeting.UpdateMeetingDto
import com.meetme.domain.dto.meeting.MeetingDto
import com.meetme.service.meeting.mapper.MeetingToMeetingDto
import com.meetme.util.tryExecute
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/meetings")
class MeetingController {

    @Autowired
    private lateinit var meetingService: MeetingService

    @Autowired
    private lateinit var meetingToMeetingDto: MeetingToMeetingDto

    @PostMapping("/create")
    fun createMeeting(@RequestBody createMeetingDto: CreateMeetingDto): DataResponse<MeetingDto> =
        tryExecute {
            val newMeeting = meetingService.create(createMeetingDto)
            meetingToMeetingDto(newMeeting, null)
        }

    @GetMapping("/{meeting_id}")
    fun getMeeting(@PathVariable("meeting_id") meetingId: Long): DataResponse<MeetingDto> =
        tryExecute {
            meetingToMeetingDto(meetingService.get(meetingId), null)
        }

    @PostMapping("/{meeting_id}/edit")
    fun editMeeting(
        @PathVariable("meeting_id") meetingId: Long,
        @RequestBody changes: UpdateMeetingDto
    ): DataResponse<MeetingDto> =
        tryExecute {
            meetingToMeetingDto(meetingService.update(meetingId, changes), null)
        }

    @DeleteMapping("/{meeting_id}")
    fun deleteMeeting(@PathVariable("meeting_id") meetingId: Long): DataResponse<Unit?> =
        tryExecute {
            meetingService.deleteByIdentifier(meetingId)
            null
        }
}