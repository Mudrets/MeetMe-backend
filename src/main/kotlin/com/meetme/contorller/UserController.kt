package com.meetme.contorller

import com.meetme.domain.dto.DataResponse
import com.meetme.domain.dto.auth.LoginCredentialsDto
import com.meetme.domain.dto.auth.RegisterCredentialsDto
import com.meetme.domain.dto.auth.UserDto
import com.meetme.domain.dto.user.EditUserDto
import com.meetme.domain.filter.NameFilter
import com.meetme.mapper.UserToUserDto
import com.meetme.services.auth.User
import com.meetme.services.auth.UserService
import com.meetme.tryExecute
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.websocket.server.PathParam

@RestController
@RequestMapping("/api/v1/user")
class UserController {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var userToUserDto: UserToUserDto

    @Autowired
    private lateinit var nameFilter: NameFilter

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
    fun getFriends(
        @PathVariable(name = "user_id") userId: Long,
    ): DataResponse<List<UserDto>> =
        tryExecute {
            userService.getFriends(userId).asSequence()
                .map(userToUserDto)
                .sortedBy(UserDto::fullName)
                .toList()
        }

    @GetMapping("/{user_id}/friends/to")
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

    @GetMapping("/{user_id}/friends/from")
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

    @GetMapping("/{user_id}")
    fun getUser(@PathVariable("user_id") userId: Long): DataResponse<User> =
        tryExecute {
            userService.getUser(userId)
        }

    @PostMapping("{user_id}/edit")
    fun editUser(
        @PathVariable("user_id") userId: Long,
        @RequestBody editUserDto: EditUserDto,
    ): DataResponse<UserDto> =
        tryExecute {
            userToUserDto(userService.editUser(userId, editUserDto))
        }

    @GetMapping("{user_id}/friends/search")
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

    @PostMapping("/{user_id}/image")
    fun uploadImage(
        @RequestParam("image") image: MultipartFile,
        @PathVariable("user_id") userId: Long,
    ): DataResponse<UserDto> =
        tryExecute {
            userToUserDto(userService.uploadImage(image, userId))
        }
}

