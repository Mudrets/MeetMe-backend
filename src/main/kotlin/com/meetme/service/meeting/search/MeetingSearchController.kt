package com.meetme.service.meeting.search

import com.meetme.domain.service.search.SearchForEntityService
import com.meetme.domain.dto.DataResponse
import com.meetme.domain.dto.meeting.MeetingDto
import com.meetme.domain.dto.meeting.SearchMeetingDto
import com.meetme.domain.dto.meeting.enums.MeetingType
import com.meetme.domain.service.search.GlobalSearchForEntityService
import com.meetme.db.meeting.Meeting
import com.meetme.service.meeting.mapper.MeetingToMeetingDto
import com.meetme.util.tryExecute
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/meetings")
class MeetingSearchController {

    @Qualifier("meetingSearchForUserService")
    @Autowired
    private lateinit var meetingSearchForUserService: SearchForEntityService<Long, SearchMeetingDto, Meeting>

    @Autowired
    private lateinit var meetingGlobalSearchForEntityServiceService: GlobalSearchForEntityService<Long, SearchMeetingDto, Meeting>

    @Autowired
    private lateinit var meetingToMeetingDto: MeetingToMeetingDto

    private fun searchPlannedMeetings(
        userId: Long,
        searchMeetingDto: SearchMeetingDto = SearchMeetingDto()
    ): Map<String, List<MeetingDto>> {
        searchMeetingDto.type = MeetingType.PLANNED
        return meetingGlobalSearchForEntityServiceService.search(userId, searchMeetingDto)
            .entries
            .associate { (key, meetings) ->
                key.typeName to meetings.map { meeting -> meetingToMeetingDto(meeting, userId) }
            }
    }

    private fun searchVisitedMeetings(
        userId: Long,
        searchMeetingDto: SearchMeetingDto = SearchMeetingDto()
    ): List<MeetingDto> {
        searchMeetingDto.type = MeetingType.VISITED
        return meetingSearchForUserService.search(userId, searchMeetingDto)
            .map { meeting -> meetingToMeetingDto(meeting, userId) }
    }

    @PostMapping("/{user_id}/planned/search")
    fun searchPlanned(
        @PathVariable("user_id") userId: Long,
        @RequestBody searchMeetingDto: SearchMeetingDto
    ): DataResponse<Map<String, List<MeetingDto>>> =
        tryExecute {
            searchPlannedMeetings(userId, searchMeetingDto)
        }

    @PostMapping("/{user_id}/visited/search")
    fun searchVisited(
        @PathVariable("user_id") userId: Long,
        @RequestBody searchMeetingDto: SearchMeetingDto,
    ): DataResponse<List<MeetingDto>> =
        tryExecute {
            searchVisitedMeetings(userId, searchMeetingDto)
        }

    @GetMapping("/{user_id}/planned")
    fun getPlannedMeetings(@PathVariable("user_id") userId: Long): DataResponse<List<MeetingDto>> =
        tryExecute {
            val query = SearchMeetingDto(type = MeetingType.PLANNED)
            meetingSearchForUserService.search(userId, query)
                .map { meeting -> meetingToMeetingDto(meeting, userId) }
        }

    @GetMapping("/{user_id}/visited")
    fun getVisitedMeetings(@PathVariable("user_id") userId: Long): DataResponse<List<MeetingDto>> =
        tryExecute {
            searchVisitedMeetings(userId)
        }
}