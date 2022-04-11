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

@RestController
@RequestMapping("/api/v1/groups")
class GroupSearchController {

    @Autowired
    private lateinit var groupGlobalSearchForEntityService: GlobalSearchForEntityService<Long, SearchGroupDto, Group>

    @Autowired
    private lateinit var groupToGroupDto: GroupToGroupDto

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

    @PostMapping("/{user_id}/search")
    fun searchGroups(
        @PathVariable("user_id") userId: Long,
        @RequestBody searchMeetingDto: SearchGroupDto
    ): DataResponse<Map<String, List<GroupDto>>> =
        tryExecute {
            searchPlannedMeetings(userId, searchMeetingDto)
        }

    @GetMapping("/user/{user_id}")
    fun getGroups(@PathVariable("user_id") userId: Long): DataResponse<List<GroupDto>> =
        tryExecute {
            searchPlannedMeetings(userId)[FilterType.MY_FILTER.typeName] ?: listOf()
        }
}