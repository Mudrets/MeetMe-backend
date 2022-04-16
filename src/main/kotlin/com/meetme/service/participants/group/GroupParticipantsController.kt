package com.meetme.service.participants.group

import com.meetme.domain.dto.DataResponse
import com.meetme.domain.dto.auth.UserDto
import com.meetme.domain.dto.goup.GroupDto
import com.meetme.db.group.Group
import com.meetme.service.group.mapper.GroupToGroupDto
import com.meetme.service.participants.base.ParticipantsService
import com.meetme.util.tryExecute
import com.meetme.service.user.mapper.UserToUserDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.bind.annotation.*

/**
 * Контроллер, обрабатывающий запросы, связанные с участниками группы.
 */
@RestController
@RequestMapping("/api/v1/groups/{group_id}/participants")
class GroupParticipantsController @Autowired constructor(
    /**
     * Сервис для работы с участниками группы.
     */
    @Qualifier("groupParticipantsService")
    private val groupParticipantsService: ParticipantsService<Group>,
    /**
     * Маппер, преобразующий User в UserDto.
     */
    private val userToUserDto: UserToUserDto,
    /**
     * Маппер, преобразующий Group в GroupDto.
     */
    private val groupToGroupDto: GroupToGroupDto,
) {

    /**
     * Обработчик HTTP GET запроса по url /api/v1/groups/{group_id}/participants для получения
     * списка участников группы.
     * @param groupId идентификатор группы.
     */
    @GetMapping
    fun getParticipants(@PathVariable("group_id") groupId: Long): DataResponse<List<UserDto>> =
        tryExecute {
            groupParticipantsService.getParticipants(groupId)
                .map(userToUserDto)
                .sortedBy(UserDto::fullName)
        }

    /**
     * Обработчик HTTP POST запроса по url /api/v1/groups/{group_id}/participants/{user_id} для добавляния
     * нового участника в группу.
     * @param groupId идентификатор группы.
     * @param userId идентификатор добавляемого пользователя.
     */
    @PostMapping("/{user_id}")
    fun addParticipant(
        @PathVariable("group_id") groupId: Long,
        @PathVariable("user_id") userId: Long,
    ): DataResponse<GroupDto> =
        tryExecute {
            groupToGroupDto(groupParticipantsService.addParticipant(userId, groupId))
        }

    /**
     * Обработчик HTTP POST запроса по url /api/v1/groups/{group_id}/participants для добавление
     * участников в группу.
     * @param groupId идентификатор группы.
     * @param userIds список идентификаторов добавляемых пользователей.
     */
    @PostMapping
    fun addParticipants(
        @PathVariable("group_id") groupId: Long,
        @RequestBody userIds: List<Long>,
    ): DataResponse<Unit?> =
        tryExecute {
            groupParticipantsService.addParticipants(userIds, groupId)
            null
        }

    /**
     * Обработчик HTTP DELETE запроса по url /api/v1/groups/{group_id}/participants/{user_id} для удаления
     * участника из группы.
     * @param groupId идентификатор группы.
     * @param userId идентификатор добавляемого пользователя.
     */
    @DeleteMapping("/{user_id}")
    fun removeParticipant(
        @PathVariable("group_id") groupId: Long,
        @PathVariable("user_id") userId: Long,
    ): DataResponse<GroupDto> =
        tryExecute {
            groupToGroupDto(groupParticipantsService.removeParticipant(userId, groupId))
        }
}