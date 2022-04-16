package com.meetme.service.group

import com.meetme.domain.dto.goup.CreateGroupDto
import com.meetme.domain.dto.DataResponse
import com.meetme.domain.dto.goup.UpdateGroupDto
import com.meetme.domain.dto.goup.GroupDto
import com.meetme.domain.dto.meeting.MeetingDto
import com.meetme.service.group.mapper.GroupToGroupDto
import com.meetme.service.meeting.mapper.MeetingToMeetingDto
import com.meetme.util.tryExecute
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * Контроллер, обрабатывающий запросы для работы с группами.
 */
@RestController
@RequestMapping("/api/v1/groups")
class GroupController @Autowired constructor(
    private val groupService: GroupServiceImpl,
    private val groupToGroupDto: GroupToGroupDto,
    private val meetingToMeetingDto: MeetingToMeetingDto,
) {
    /**
     * Обработчик HTTP POST запроса по url /api/v1/groups/create для создания группы.
     * @param createGroupDto данные для создания новой группы.
     */
    @PostMapping("/create")
    fun createGroup(@RequestBody createGroupDto: CreateGroupDto): DataResponse<GroupDto> =
        tryExecute {
            groupToGroupDto(groupService.create(createGroupDto))
        }

    /**
     * Обработчик HTTP POST запроса по url /api/v1/groups/{group_id}/edit для редактирования группы.
     * @param groupId идентификатор пользователя.
     * @param editCredentials новые данные изменяемой группы.
     */
    @PostMapping("/{group_id}/edit")
    fun editGroup(
        @PathVariable("group_id") groupId: Long,
        @RequestBody editCredentials: UpdateGroupDto,
    ): DataResponse<GroupDto> =
        tryExecute {
            groupToGroupDto(groupService.update(groupId, editCredentials))
        }

    /**
     * Обработчик HTTP GET запроса по url /api/v1/groups/{group_id} для данных о группе.
     * @param groupId идентификатор группы.
     */
    @GetMapping("/{group_id}")
    fun getGroup(@PathVariable("group_id") groupId: Long): DataResponse<GroupDto> =
        tryExecute {
            groupToGroupDto(groupService.get(groupId))
        }

    /**
     * Обработчик HTTP DELETE запроса по url /api/v1/groups/{group_id}/{user_id} для удаления группы.
     * @param groupId идентификатор удаляемой группы.
     */
    @DeleteMapping("/{group_id}/{user_id}")
    fun deleteGroup(
        @PathVariable("group_id") groupId: Long,
        @PathVariable("user_id") userId: Long,
    ): DataResponse<Unit?> =
        tryExecute {
            groupService.deleteByIdentifier(groupId)
            null
        }

    /**
     * Обработчик HTTP GET запроса по url /api/v1/groups/{group_id}/meetings для получения мероприятий, в которых
     * участвует группа.
     * @param groupId идентификатор группы.
     */
    @GetMapping("/{group_id}/meetings")
    fun getMeetingsOfGroup(@PathVariable("group_id") groupId: Long): DataResponse<List<MeetingDto>> =
        tryExecute {
            groupService.getMeetings(groupId)
                .map { meeting -> meetingToMeetingDto(meeting, null) }
        }
}