package com.meetme.auth

import com.meetme.friends.Friendship
import com.meetme.friends.FriendshipDao
import com.meetme.friends.FriendshipService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService : UserDetailsService {

    private val logger: Logger = LoggerFactory.getLogger(UserService::class.java)

    @Autowired
    private lateinit var userDao: UserDao

    @Autowired
    private lateinit var friendshipService: FriendshipService

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    override fun loadUserByUsername(username: String): UserDetails? = loadUserByEmail(username)

    @Throws(NoSuchElementException::class)
    private inline fun <T> Long.doIfExist(action: (User) -> T): T {
        val dbUser = getUserById(this)
        if (dbUser != null)
            return action(dbUser)
        else
            throw NoSuchElementException("User with id = $this not found")
    }

    private fun getUserById(id: Long): User? {
        val userOpt = userDao.findById(id)
        return if (userOpt.isPresent) {
            val dbUser = userOpt.get()
            logger.debug("User $dbUser found by id: $id")
            dbUser
        } else {
            logger.debug("User not found by id: $id")
            return null
        }
    }

    fun createNewUserByEmailAndPass(email: String, password: String): User? {
        if (loadUserByUsername(email) != null) return null

        val newUser = userDao.save(
            User(
                email = email,
                password = passwordEncoder.encode(password)
            )
        )

        logger.debug("User $newUser created")
        return newUser
    }

    fun loadUserByEmail(email: String): User? {
        val dbUser = userDao.findByEmail(email)

        if (dbUser != null)
            logger.debug("User $dbUser found by email: $email")
        else
            logger.debug("User not found by email: $email")

        return dbUser
    }

    fun checkPassword(user: User, password: String): Boolean = passwordEncoder.matches(password, user.password)

    @Throws(IllegalArgumentException::class)
    fun addFriend(userId: Long, friendId: Long): Friendship {
        val dbUser = getUserById(userId)
        val dbFriend = getUserById(friendId)

        return if (dbUser != null && dbFriend != null)
            friendshipService.createNewFriendship(dbUser, dbFriend)
        else
            throw IllegalArgumentException(
                if (dbUser == null && dbFriend == null)
                    "Users with id = $userId and id = $friendId do not exist"
                else if (dbUser == null)
                    "User with id = $userId does not exist"
                else
                    "User with id = $friendId does not exist"
            )
    }

    @Throws(IllegalArgumentException::class)
    fun removeFriend(userId: Long, friendId: Long) {
        val dbUser = getUserById(userId)
        val dbFriend = getUserById(friendId)

        if (dbUser != null && dbFriend != null)
            friendshipService.removeFriendShip(dbUser, dbFriend)
        else
            throw IllegalArgumentException(
                if (dbUser == null && dbFriend == null)
                    "Users with id = $userId and id = $friendId do not exist"
                else if (dbUser == null)
                    "User with id = $userId does not exist"
                else
                    "User with id = $friendId does not exist"
            )
    }

    fun getFriends(userId: Long): List<User> =
        userId.doIfExist { user ->
            friendshipService.getFriendsOfUser(user)
        }

    fun getFriendsRequestToUser(userId: Long): List<User> =
        userId.doIfExist { user ->
            friendshipService.getFriendRequestToUser(user)
        }

    fun getFriendsRequestFromUser(userId: Long) =
        userId.doIfExist { user ->
            friendshipService.getFriendRequestFromUser(user)
        }

    fun changeName(userId: Long, newName: String): User? {
        val dbUser = getUserById(userId)
        dbUser?.name = newName
        dbUser?.let { userDao.save(it) }
        return dbUser
    }
}