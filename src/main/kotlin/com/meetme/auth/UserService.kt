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
import java.util.Date

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

    private fun getName(fullName: String): String = fullName.split(' ')[0]

    private fun getSurname(fullName: String): String =
        if (fullName.trim().contains(' '))
            fullName.split(' ')[1]
        else
            ""

    fun createNewUserByEmailAndPass(email: String, password: String, fullName: String): User {
        if (userDao.findByEmail(email) != null)
            throw IllegalArgumentException("User with email $email already exists")

        val newUser = userDao.save(
            User(
                email = email,
                password = passwordEncoder.encode(password),
                name = getName(fullName),
                surname = getSurname(fullName),
            )
        )

        logger.debug("User $newUser created")
        return newUser
    }

    private fun loadUserByEmail(email: String): User {
        val dbUser = userDao.findByEmail(email)

        if (dbUser != null) {
            logger.debug("User $dbUser found by email: $email")
        } else {
            logger.debug("User not found by email: $email")
            throw IllegalArgumentException("User not found by email: $email")
        }

        return dbUser
    }

    fun loginUserByEmailAndPassword(email: String, password: String): User {
        val user = loadUserByEmail(email)
        if (!checkPassword(user, password))
            throw IllegalArgumentException("Incorrect password")
        return user
    }

    fun checkPassword(user: User, password: String): Boolean = passwordEncoder.matches(password, user.password)

    @Throws(IllegalArgumentException::class)
    fun addFriend(userId: Long, friendId: Long): Friendship =
        (userId to friendId).doIfExist(userDao, logger) { user, friend ->
            friendshipService.createNewFriendship(user, friend)
        }


    @Throws(IllegalArgumentException::class)
    fun removeFriend(userId: Long, friendId: Long) =
        (userId to friendId).doIfExist(userDao, logger) { user, friend ->
            friendshipService.removeFriendShip(user, friend)
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

    fun searchFriends(searchQuery: String): List<User> =
        userDao.findAll()
            .filter { user -> user.fullname.contains(searchQuery) }
            .sortedBy(User::fullname)
}