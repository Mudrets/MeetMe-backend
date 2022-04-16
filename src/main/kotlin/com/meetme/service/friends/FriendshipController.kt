package com.meetme.service.friends

import com.meetme.domain.dto.DataResponse
import com.meetme.domain.dto.auth.UserDto
import com.meetme.domain.filter.Filter
import com.meetme.service.user.mapper.UserToUserDto
import com.meetme.util.tryExecute
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * Контроллер, обрабатывающий запросы связанные со списком друзей пользователя.
 */
@RestController
@RequestMapping("/api/v1/user/{user_id}/friends")
class FriendshipController @Autowired constructor(
    /**
     * Маппер, преобразуюзий User в UserDto.
     */
    private val userToUserDto: UserToUserDto,
    /**
     * Сервис для работы с дружескими отношениями между пользователями.
     */
    private val friendshipService: FriendshipService,
    /**
     * Фильтр для фильтрации по имени.
     */
    private val userFilter: Filter<UserDto, String>,
) {
    /**
     * Обработчик HTTP GЕT запроса по url /api/v1/user/{user_id}/friends/to для получения заявок в друзья
     * пользователю.
     * @param userId идентификатор пользователя, для которого требуется список заявок в друзья.
     * @param searchQuery поисковой запрос для поиска.
     */
    @GetMapping("/to")
    fun getFriendRequestTo(
        @PathVariable(name = "user_id") userId: Long,
        @RequestParam("query") searchQuery: String,
    ): DataResponse<List<UserDto>> =
        tryExecute {
            friendshipService.getFriendRequestToUser(userId).asSequence()
                .map(userToUserDto)
                .filter { friend -> userFilter(friend, searchQuery) }
                .sortedBy(UserDto::fullName)
                .toList()
        }

    /**
     * Обработчик HTTP GЕT запроса по url /api/v1/user/{user_id}/friends/from для получения заявок в друзья
     * от пользователя.
     * @param userId идентификатор пользователя, от которого требуется список заявок в друзья.
     * @param searchQuery поисковой запрос для поиска.
     */
    @GetMapping("/from")
    fun getFriendRequestFrom(
        @PathVariable(name = "user_id") userId: Long,
        @RequestParam("query") searchQuery: String,
    ): DataResponse<List<UserDto>> =
        tryExecute {
            friendshipService.getFriendRequestFromUser(userId).asSequence()
                .map(userToUserDto)
                .filter { friend -> userFilter(friend, searchQuery) }
                .sortedBy(UserDto::fullName)
                .toList()
        }

    /**
     * Обработчик HTTP GЕT запроса по url /api/v1/user/{user_id}/friends для получения друзей пользователя.
     * @param userId идентификатор пользователя, для которого требуется список друзей.
     */
    @GetMapping
    fun getFriends(
        @PathVariable(name = "user_id") userId: Long,
    ): DataResponse<List<UserDto>> =
        tryExecute {
            friendshipService.getFriendsOfUser(userId).asSequence()
                .map(userToUserDto)
                .sortedBy(UserDto::fullName)
                .toList()
        }

    /**
     * Обработчик HTTP GЕT запроса по url /api/v1/user/{user_id}/friends/search для поиска среди друзей пользователя
     * и глобального списка пользователей.
     * @param userId идентификатор пользователя, для которого требуется список друзей.
     * @param searchQuery поисковой запрос для поиска.
     */
    @GetMapping("/search")
    fun search(
        @PathVariable("user_id") userId: Long,
        @RequestParam("query") searchQuery: String,
    ): DataResponse<Map<String, List<UserDto>>> =
        tryExecute {
            friendshipService.getAllPeopleWithFriends(userId).entries
                .associate { (key, users) ->
                    key.status to users
                        .map(userToUserDto)
                        .filter { friend -> userFilter(friend, searchQuery) }
                }
        }

    /**
     * Обработчик HTTP POST запроса по url /api/v1/user/{user_id}/friends/{friend_id} для
     * добавления в список друзей пользователя.
     * @param userId идентификатор пользователя.
     * @param friendId идентификатор пользователя, который добавляется в друзья.
     */
    @PostMapping("/{friend_id}")
    fun addFriend(
        @PathVariable(name = "user_id") userId: Long,
        @PathVariable(name = "friend_id") friendId: Long,
    ): DataResponse<Unit?> =
        tryExecute {
            friendshipService.sendRequestFrom1To2(userId, friendId)
            null
        }

    /**
     * Обработчик HTTP DELETE запроса по url /api/v1/user/{user_id}/friends/{friend_id} для
     * удаления из списка друзей пользователя.
     * @param userId идентификатор пользователя.
     * @param friendId идентификатор пользователя, который удаляется из друзей.
     */
    @DeleteMapping("/{friend_id}")
    fun removeFriend(
        @PathVariable(name = "user_id") userId: Long,
        @PathVariable(name = "friend_id") friendId: Long,
    ): DataResponse<Unit?> =
        tryExecute {
            friendshipService.removeRequestFrom1(userId, friendId)
            null
        }
}