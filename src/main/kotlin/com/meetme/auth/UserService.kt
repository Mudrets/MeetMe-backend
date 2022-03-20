package com.meetme.auth

import com.meetme.doIfExist
import com.meetme.friends.Friendship
import com.meetme.friends.FriendshipService
import com.meetme.getEntity
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
        val dbUser = userId.getEntity(userDao, logger)
        val dbFriend = userId.getEntity(userDao, logger)

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
        val dbUser = userId.getEntity(userDao, logger)
        val dbFriend = userId.getEntity(userDao, logger)

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
        userId.doIfExist(userDao, logger) { user ->
            friendshipService.getFriendsOfUser(user)
        }

    fun getFriendsRequestToUser(userId: Long): List<User> =
        userId.doIfExist(userDao, logger) { user ->
            friendshipService.getFriendRequestToUser(user)
        }

    fun getFriendsRequestFromUser(userId: Long) =
        userId.doIfExist(userDao, logger) { user ->
            friendshipService.getFriendRequestFromUser(user)
        }

    fun changeName(userId: Long, newName: String): User? {
        val dbUser = userId.getEntity(userDao, logger)
        dbUser?.name = newName
        dbUser?.let { userDao.save(it) }
        return dbUser
    }

    fun getUser(userId: Long): User =
        userId.doIfExist(userDao, logger) { user -> user }
}