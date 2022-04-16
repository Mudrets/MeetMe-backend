package com.meetme.service.group.search

import com.meetme.domain.dto.DataResponse
import com.meetme.domain.dto.goup.GroupDto
import com.meetme.domain.dto.goup.SearchGroupDto
import com.meetme.domain.filter.FilterType
import com.meetme.domain.service.search.GlobalSearchForEntityService
import com.meetme.db.group.Group
import com.meetme.service.group.mapper.GroupToGroupDto
import com.meetme.util.tryExecute
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * Контроллер, обрабатывающий запросы для поиска групп.
 */
@RestController
@RequestMapping("/api/v1/groups")
class GroupSearchController @Autowired constructor(
    private val groupGlobalSearchForEntityService: GlobalSearchForEntityService<Long, SearchGroupDto, Group>,
    private val groupToGroupDto: GroupToGroupDto,
) {

    /**
     * Ищет группы по переданному поисковому запросу.
     * @param userId идентификатор пользователя, для которого производится поиск.
     * @param searchMeetingDto данные для поиска.
     * @return Возвращает Map<FilterType, List<SearchedEntity>> где по ключу FilterType.MY_FILTER
     * находится результат локального поиска, а по ключу FilterType.MY_FILTER результат глобального
     * поиска.
     */
    private fun searchPlannedMeetings(
        userId: Long,
        searchMeetingDto: SearchGroupDto = SearchGroupDto()
    ): Map<String, List<GroupDto>> {
        return groupGlobalSearchForEntityService.search(userId, searchMeetingDto)
            .entries
            .associate { (key, groups) ->
                key.typeName to groups.map(groupToGroupDto)
            }
    }

    /**
     * Обработчик HTTP POST запроса по url /api/v1/groups/{user_id}/search для поиска групп по переданному
     * поисковому запросу.
     * @param userId идентификатор пользователя.
     * @param searchMeetingDto поисковой запрос.
     */
    @PostMapping("/{user_id}/search")
    fun searchGroups(
        @PathVariable("user_id") userId: Long,
        @RequestBody searchMeetingDto: SearchGroupDto
    ): DataResponse<Map<String, List<GroupDto>>> =
        tryExecute {
            searchPlannedMeetings(userId, searchMeetingDto)
        }

    /**
     * Обработчик HTTP GET запроса по url /api/v1/groups/{user_id}/search для получения групп пользователя.
     * поисковому запросу.
     * @param userId идентификатор пользователя.
     */
    @GetMapping("/user/{user_id}")
    fun getGroups(@PathVariable("user_id") userId: Long): DataResponse<List<GroupDto>> =
        tryExecute {
            searchPlannedMeetings(userId)[FilterType.MY_FILTER.typeName] ?: listOf()
        }
}