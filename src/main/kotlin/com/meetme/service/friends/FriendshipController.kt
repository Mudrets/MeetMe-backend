package com.meetme.service.friends

import com.meetme.domain.dto.DataResponse
import com.meetme.domain.dto.auth.UserDto
import com.meetme.domain.filter.Filter
import com.meetme.service.user.mapper.UserToUserDto
import com.meetme.util.tryExecute
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/user/{user_id}/friends")
class FriendshipController {

    @Autowired
    private lateinit var userToUserDto: UserToUserDto

    @Autowired
    private lateinit var friendshipService: FriendshipService

    @Autowired
    private lateinit var userFilter: Filter<UserDto, String>

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

    @GetMapping()
    fun getFriends(
        @PathVariable(name = "user_id") userId: Long,
    ): DataResponse<List<UserDto>> =
        tryExecute {
            friendshipService.getFriendsOfUser(userId).asSequence()
                .map(userToUserDto)
                .sortedBy(UserDto::fullName)
                .toList()
        }

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

    @PostMapping("/{friend_id}")
    fun addFriend(
        @PathVariable(name = "user_id") userId: Long,
        @PathVariable(name = "friend_id") friendId: Long,
    ): DataResponse<Unit?> =
        tryExecute {
            friendshipService.sendRequestFrom1To2(userId, friendId)
            null
        }

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