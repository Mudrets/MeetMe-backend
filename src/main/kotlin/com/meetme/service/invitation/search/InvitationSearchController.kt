package com.meetme.service.invitation.search

import com.meetme.domain.dto.DataResponse
import com.meetme.domain.dto.meeting.MeetingDto
import com.meetme.domain.dto.meeting.SearchMeetingDto
import com.meetme.service.meeting.mapper.MeetingToMeetingDto
import com.meetme.util.tryExecute
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * Обработчик запросов для поиска
 */
@RestController
@RequestMapping("/api/v1/meetings/{user_id}/invites")
class InvitationSearchController @Autowired constructor(
    /**
     * Сервис для поиска приглашений на мероприятия.
     */
    private val invitationSearchService: InvitationSearchService,
    /**
     * Маппер, преобразующий Meeting в MeetingDto.
     */
    private val meetingToMeetingDto: MeetingToMeetingDto,
) {

    /**
     * Обработчик HTTP POST запроса по url /api/v1/meetings/{user_id}/invites/search для поиска
     * приглашений на мероприятия.
     * @param userId идентификатор пользователя, для которого ищутся приглашения.
     * @param searchMeetingDto посиковой запрос.
     */
    @PostMapping("/search")
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

    /**
     * Обработчик HTTP GET запроса по url /api/v1/meetings/{user_id}/invites для получения списка
     * всех мероприятий, на которые приглашен пользователь.
     * @param userId идентификатор пользвоателя, для которого получается список приглашений.
     */
    @GetMapping
    fun getInvitesOnMeetings(@PathVariable("user_id") userId: Long): DataResponse<Map<String, List<MeetingDto>>> =
        tryExecute {
            invitationSearchService.search(userId, SearchMeetingDto()).entries
                .associate { (key, meetings) ->
                    key to meetings.map { meeting -> meetingToMeetingDto(meeting, userId) }
                }
        }
}