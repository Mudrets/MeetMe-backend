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

/**
 * Контроллер, для работы с поиском мероприятий.
 */
@RestController
@RequestMapping("/api/v1/meetings")
class MeetingSearchController @Autowired constructor(
    /**
     * Сервис для поиска мероприятий для пользователя.
     */
    @Qualifier("meetingSearchForUserService")
    private val meetingSearchForUserService: SearchForEntityService<Long, SearchMeetingDto, Meeting>,
    /**
     * Сервис для глобального поиска мероприятий.
     */
    private val meetingGlobalSearchForEntityServiceService: GlobalSearchForEntityService<Long, SearchMeetingDto, Meeting>,
    /**
     * Маппер, преобзращующий Meeting в MeetingDto.
     */
    private val meetingToMeetingDto: MeetingToMeetingDto,
) {
    /**
     * Ищет запланированные мероприятия для пользователя с переданным идентификатором.
     * @param userId идентификатор пользователя.
     * @param searchMeetingDto поисковой запрос.
     * @return Возвращает Map<String, List<SearchedEntity>> где по ключу my
     * находится результат локального поиска, а по ключу global результат глобального
     * поиска.
     */
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

    /**
     * Ищет посещенные пользователем мероприятия.
     * @param userId идентификатор пользователя.
     * @param searchMeetingDto поисковой запрос.
     * @return Возвращает List<MeetingDto> содержащий в себе информацию
     * обо всех посещенных мероприятиях, удовлетворяющих поисковым запросам.
     */
    private fun searchVisitedMeetings(
        userId: Long,
        searchMeetingDto: SearchMeetingDto = SearchMeetingDto()
    ): List<MeetingDto> {
        searchMeetingDto.type = MeetingType.VISITED
        return meetingSearchForUserService.search(userId, searchMeetingDto)
            .map { meeting -> meetingToMeetingDto(meeting, userId) }
    }

    /**
     * Обработчик HTTP POST запроса по url /api/v1/meetings/{user_id}/planned/search для посика
     * запланированных мероприятий пользователей.
     * @param userId идентификатор пользователя.
     * @param searchMeetingDto поисковыой запрос.
     */
    @PostMapping("/{user_id}/planned/search")
    fun searchPlanned(
        @PathVariable("user_id") userId: Long,
        @RequestBody searchMeetingDto: SearchMeetingDto
    ): DataResponse<Map<String, List<MeetingDto>>> =
        tryExecute {
            searchPlannedMeetings(userId, searchMeetingDto)
        }

    /**
     * Обработчик HTTP POST запроса по url /api/v1/meetings/{user_id}/visited/search для поиска
     * посещенных мероприятий пользователя.
     * @param userId идентификатор пользователя.
     * @param searchMeetingDto поисковыой запрос.
     */
    @PostMapping("/{user_id}/visited/search")
    fun searchVisited(
        @PathVariable("user_id") userId: Long,
        @RequestBody searchMeetingDto: SearchMeetingDto,
    ): DataResponse<List<MeetingDto>> =
        tryExecute {
            searchVisitedMeetings(userId, searchMeetingDto)
        }

    /**
     * Обработчик HTTP POST запроса по url /api/v1/meetings/{user_id}/planned для получения
     * запланированных мероприятий пользователя.
     * @param userId идентификатор пользователя.
     */
    @GetMapping("/{user_id}/planned")
    fun getPlannedMeetings(@PathVariable("user_id") userId: Long): DataResponse<List<MeetingDto>> =
        tryExecute {
            val query = SearchMeetingDto(type = MeetingType.PLANNED)
            meetingSearchForUserService.search(userId, query)
                .map { meeting -> meetingToMeetingDto(meeting, userId) }
        }

    /**
     * Обработчик HTTP POST запроса по url /api/v1/meetings/{user_id}/visited для получения
     * посещенных мероприятий пользователя.
     * @param userId идентификатор пользователя.
     */
    @GetMapping("/{user_id}/visited")
    fun getVisitedMeetings(@PathVariable("user_id") userId: Long): DataResponse<List<MeetingDto>> =
        tryExecute {
            searchVisitedMeetings(userId)
        }
}