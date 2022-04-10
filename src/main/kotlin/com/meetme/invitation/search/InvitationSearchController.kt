package com.meetme.invitation.search

import com.meetme.domain.dto.DataResponse
import com.meetme.domain.dto.meeting.MeetingDto
import com.meetme.domain.dto.meeting.SearchMeetingDto
import com.meetme.meeting.mapper.MeetingToMeetingDto
import com.meetme.tryExecute
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/meetings/{user_id}/invites")
class InvitationSearchController {

    @Autowired
    private lateinit var invitationSearchService: InvitationSearchService

    @Autowired
    private lateinit var meetingToMeetingDto: MeetingToMeetingDto

    @GetMapping("/search")
    fun searchInvites(
        @PathVariable("user_id") userId: Long,
        @RequestBody searchMeetingDto: SearchMeetingDto,
    ): DataResponse<Map<String, List<MeetingDto>>> =
        tryExecute {
            invitationSearchService.search(userId, searchMeetingDto).entries
                .associate { (key, meetings) ->
                    key to meetings.map { meeting -> meetingToMeetingDto(meeting, userId) }
                }
        }

    @GetMapping
    fun getInvitesOnMeetings(@PathVariable("user_id") userId: Long): DataResponse<Map<String, List<MeetingDto>>> =
        tryExecute {
            invitationSearchService.search(userId, SearchMeetingDto()).entries
                .associate { (key, meetings) ->
                    key to meetings.map { meeting -> meetingToMeetingDto(meeting, userId) }
                }
        }
}