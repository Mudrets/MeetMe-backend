package com.meetme.contorller

import com.meetme.auth.User
import com.meetme.dto.auth.AuthorizationUserDto
import com.meetme.dto.auth.CredentialsDto
import com.meetme.auth.UserService
import com.meetme.data.DataResponse
import com.meetme.dto.friends.FriendDto
import com.meetme.friends.Friendship
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
@RestController
@RequestMapping("/api/v1/user")
class UserController {

    @Autowired
    private lateinit var userService: UserService

    @PostMapping("/register")
    fun register(@RequestBody credentials: CredentialsDto): DataResponse<AuthorizationUserDto> {
        val newUser = userService.createNewUserByEmailAndPass(email = credentials.email, password = credentials.password)
        return if (newUser != null)
            DataResponse(data = AuthorizationUserDto(id = newUser.id))
        else
            DataResponse(message = "User already exist")
    }

    @PostMapping("/login")
    fun login(@RequestBody credentials: CredentialsDto): DataResponse<AuthorizationUserDto> {
        val dbUser = userService.loadUserByEmail(credentials.email)
            ?: return DataResponse(message = "User does not exist")

        return if (userService.checkPassword(user = dbUser, password = credentials.password))
            DataResponse(data = AuthorizationUserDto(id = dbUser.id))
        else
            DataResponse(message = "Incorrect password")
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
    fun getFriends(@PathVariable(name = "user_id") userId: Long): DataResponse<List<FriendDto>> =
        tryExecute {
            userService.getFriends(userId).asSequence()
                .map { user ->
                    FriendDto(
                        id = user.id,
                        name = user.name,
                        surname = user.surname,
                        photoUrl = user.photoUrl
                    )
                }
                .sortedBy(FriendDto::fullName)
                .toList()
        }

    @GetMapping("/friends/to/{user_id}")
    fun getFriendRequestTo(@PathVariable(name = "user_id") userId: Long): DataResponse<List<FriendDto>> =
        tryExecute {
            userService.getFriendsRequestToUser(userId).asSequence()
                .map { user ->
                    FriendDto(
                        id = user.id,
                        name = user.name,
                        surname = user.surname,
                        photoUrl = user.photoUrl
                    )
                }
                .sortedBy(FriendDto::fullName)
                .toList()
        }

    @GetMapping("/friends/from/{user_id}")
    fun getFriendRequestFrom(@PathVariable(name = "user_id") userId: Long): DataResponse<List<FriendDto>> =
        tryExecute {
            userService.getFriendsRequestFromUser(userId).asSequence()
                .map { user ->
                    FriendDto(
                        id = user.id,
                        name = user.name,
                        surname = user.surname,
                        photoUrl = user.photoUrl
                    )
                }
                .sortedBy(FriendDto::fullName)
                .toList()
        }

    @PostMapping("{user_id}/name/{new_name}")
    fun changeName(
        @PathVariable("new_name") newName: String,
        @PathVariable("user_id") userId: Long,
    ): DataResponse<User?> =
        DataResponse(
            data = userService.changeName(userId, newName)
        )

    private inline fun <T> tryExecute(action: () -> T): DataResponse<T> {
        return try {
            DataResponse(data = action())
        } catch (e: NoSuchElementException) {
            DataResponse(message = e.message ?: "Failed to complete request")
        }
    }
}

