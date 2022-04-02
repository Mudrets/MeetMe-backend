package com.meetme.friends

import com.meetme.domain.dto.DataResponse
import com.meetme.domain.dto.auth.UserDto
import com.meetme.domain.filter.NameFilter
import com.meetme.user.mapper.UserToUserDto
import com.meetme.user.UserService
import com.meetme.tryExecute
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user/{user_id}/friends")
class FriendShipController {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var userToUserDto: UserToUserDto

    @Autowired
    private lateinit var nameFilter: NameFilter

    @GetMapping("/to")
    fun getFriendRequestTo(
        @PathVariable(name = "user_id") userId: Long,
        @RequestParam("query") searchQuery: String,
    ): DataResponse<List<UserDto>> =
        tryExecute {
            userService.getFriendsRequestToUser(userId).asSequence()
                .filter { user -> nameFilter(user, searchQuery) }
                .map(userToUserDto)
                .sortedBy(UserDto::fullName)
                .toList()
        }

    @GetMapping("/from")
    fun getFriendRequestFrom(
        @PathVariable(name = "user_id") userId: Long,
        @RequestParam("query") searchQuery: String,
    ): DataResponse<List<UserDto>> =
        tryExecute {
            userService.getFriendsRequestFromUser(userId).asSequence()
                .filter { user -> nameFilter(user, searchQuery) }
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
            val map = userService.searchFriends(userId, searchQuery)
            mutableMapOf(
                "friends" to (map[true]?.map(userToUserDto) ?: listOf()),
                "global" to (map[false]?.map(userToUserDto) ?: listOf()),
            )
        }
}