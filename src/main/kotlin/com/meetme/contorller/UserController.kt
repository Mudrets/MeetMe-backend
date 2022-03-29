package com.meetme.contorller

import com.meetme.auth.User
import com.meetme.data.dto.auth.UserDto
import com.meetme.data.dto.auth.RegisterCredentialsDto
import com.meetme.auth.UserService
import com.meetme.data.DataResponse
import com.meetme.data.dto.auth.LoginCredentialsDto
import com.meetme.data.dto.user.UserInfoDto
import com.meetme.friends.Friendship
import com.meetme.mapper.UserToUserDto
import com.meetme.tryExecute
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
@RestController
@RequestMapping("/api/v1/user")
class UserController {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var userToUserDto: UserToUserDto

    @PostMapping("/register")
    fun register(@RequestBody credentials: RegisterCredentialsDto): DataResponse<UserDto> =
        tryExecute {
            val user = userService.createNewUserByEmailAndPass(
                email = credentials.email,
                password = credentials.password,
                fullName = credentials.fullName
            )
            userToUserDto(user)
        }

    @PostMapping("/login")
    fun login(@RequestBody credentials: LoginCredentialsDto): DataResponse<UserDto> =
        tryExecute {
            val user = userService.loginUserByEmailAndPassword(
                email = credentials.email,
                password = credentials.password,
            )
            userToUserDto(user)
        }

    @PostMapping("/{user_id}/friends/{friend_id}")
    fun addFriend(
        @PathVariable(name = "user_id") userId: Long,
        @PathVariable(name = "friend_id") friendId: Long,
    ): DataResponse<Unit?> =
        tryExecute {
            userService.addFriend(userId, friendId)
            null
        }

    @DeleteMapping("/{user_id}/friends/{friend_id}")
    fun removeFriend(
        @PathVariable(name = "user_id") userId: Long,
        @PathVariable(name = "friend_id") friendId: Long,
    ): DataResponse<Unit?> =
        tryExecute {
            userService.removeFriend(userId, friendId)
            null
        }

    @GetMapping("/{user_id}/friends")
    fun getFriends(@PathVariable(name = "user_id") userId: Long): DataResponse<List<UserDto>> =
        tryExecute {
            userService.getFriends(userId).asSequence()
                .map(userToUserDto)
                .sortedBy(UserDto::fullName)
                .toList()
        }

    @GetMapping("/{user_id}/friends/to")
    fun getFriendRequestTo(@PathVariable(name = "user_id") userId: Long): DataResponse<List<UserDto>> =
        tryExecute {
            userService.getFriendsRequestToUser(userId).asSequence()
                .map(userToUserDto)
                .sortedBy(UserDto::fullName)
                .toList()
        }

    @GetMapping("/{user_id}/friends/from")
    fun getFriendRequestFrom(@PathVariable(name = "user_id") userId: Long): DataResponse<List<UserDto>> =
        tryExecute {
            userService.getFriendsRequestFromUser(userId).asSequence()
                .map(userToUserDto)
                .sortedBy(UserDto::fullName)
                .toList()
        }

    @GetMapping("/{user_id}")
    fun getUser(@PathVariable("user_id") userId: Long): DataResponse<User> =
        tryExecute {
            userService.getUser(userId)
        }

    @PostMapping("{user_id}/name/{new_name}")
    fun changeName(
        @PathVariable("new_name") newName: String,
        @PathVariable("user_id") userId: Long,
    ): DataResponse<User?> =
        DataResponse(
            data = userService.changeName(userId, newName)
        )

    @GetMapping("/{search_query}")
    fun search(@PathVariable("search_query") searchQuery: String): DataResponse<List<UserDto>> =
        tryExecute {
            userService.searchFriends(searchQuery).asSequence()
                .map(userToUserDto)
                .toList()
        }
}

