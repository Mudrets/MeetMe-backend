package com.meetme.contorller

import com.meetme.auth.User
import com.meetme.dto.auth.UserDto
import com.meetme.dto.auth.RegisterCredentialsDto
import com.meetme.auth.UserService
import com.meetme.data.DataResponse
import com.meetme.dto.auth.LoginCredentialsDto
import com.meetme.dto.user.UserInfoDto
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

    @PostMapping("/friends/{user_id}/{friend_id}")
    fun addFriend(
        @PathVariable(name = "user_id") userId: Long,
        @PathVariable(name = "friend_id") friendId: Long,
    ): DataResponse<Friendship> =
        tryExecute { userService.addFriend(userId, friendId) }

    @DeleteMapping("/friends/{user_id}/{friend_id}")
    fun removeFriend(
        @PathVariable(name = "user_id") userId: Long,
        @PathVariable(name = "friend_id") friendId: Long,
    ): DataResponse<Boolean> =
        try {
            userService.removeFriend(userId, friendId)
            DataResponse(data = true)
        } catch (e: IllegalArgumentException) {
            DataResponse(message = e.message ?: "Bad request", data = false)
        }

    @GetMapping("/friends/{user_id}")
    fun getFriends(@PathVariable(name = "user_id") userId: Long): DataResponse<List<UserInfoDto>> =
        tryExecute {
            userService.getFriends(userId).asSequence()
                .map { user ->
                    UserInfoDto(
                        id = user.id,
                        name = user.name,
                        surname = user.surname,
                        photoUrl = user.photoUrl
                    )
                }
                .sortedBy(UserInfoDto::fullName)
                .toList()
        }

    @GetMapping("/friends/to/{user_id}")
    fun getFriendRequestTo(@PathVariable(name = "user_id") userId: Long): DataResponse<List<UserInfoDto>> =
        tryExecute {
            userService.getFriendsRequestToUser(userId).asSequence()
                .map { user ->
                    UserInfoDto(
                        id = user.id,
                        name = user.name,
                        surname = user.surname,
                        photoUrl = user.photoUrl
                    )
                }
                .sortedBy(UserInfoDto::fullName)
                .toList()
        }

    @GetMapping("/friends/from/{user_id}")
    fun getFriendRequestFrom(@PathVariable(name = "user_id") userId: Long): DataResponse<List<UserInfoDto>> =
        tryExecute {
            userService.getFriendsRequestFromUser(userId).asSequence()
                .map { user ->
                    UserInfoDto(
                        id = user.id,
                        name = user.name,
                        surname = user.surname,
                        photoUrl = user.photoUrl
                    )
                }
                .sortedBy(UserInfoDto::fullName)
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
    fun search(@PathVariable("search_query") searchQuery: String): DataResponse<List<UserInfoDto>> =
        tryExecute {
            userService.searchFriends(searchQuery).asSequence()
                .map { user ->
                    UserInfoDto(
                        id = user.id,
                        name = user.name,
                        surname = user.surname,
                        photoUrl = user.photoUrl
                    )
                }
                .toList()
        }
}

