package com.meetme.contorller

import com.meetme.data.DataResponse
import com.meetme.meeting.Meeting
import com.meetme.meeting.MeetingService
import com.meetme.dto.meeting.CreateMeetingDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
}