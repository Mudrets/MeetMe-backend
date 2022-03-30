package com.meetme.auth

import com.meetme.data.dto.user.EditUserDto
import com.meetme.doIfExist
import com.meetme.friends.Friendship
import com.meetme.friends.FriendshipService
import com.meetme.getEntity
import com.meetme.iterest.InterestService
import com.meetme.medialink.MediaLinkService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@Service
class UserService : UserDetailsService {

    private val logger: Logger = LoggerFactory.getLogger(UserService::class.java)

    @Autowired
    private lateinit var userDao: UserDao

    @Autowired
    private lateinit var friendshipService: FriendshipService

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var interestService: InterestService

    @Autowired
    private lateinit var mediaLinkService: MediaLinkService

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
            .filter { user -> user.fullName.contains(searchQuery) }
            .sortedBy(User::fullName)

    fun editUser(userId: Long, editUserDto: EditUserDto): User =
        userId.doIfExist(userDao, logger) { user ->
            val interestsSet =
                interestService.convertToInterestEntityAndAddNewInterests(interests = editUserDto.interests)

            val links = mediaLinkService.createNewLinks(editUserDto.mediaLinks, user)

            user.apply {
                name = getName(editUserDto.fullName)
                surname = getSurname(editUserDto.fullName)
                description = editUserDto.description
                interests = interestsSet
                socialMediaLinks = links
            }
            userDao.save(user)
        }

    fun uploadImage(file: MultipartFile, userId: Long): User =
        userId.doIfExist(userDao, logger) { user ->
            if (!Files.exists(rootLocation))
                Files.createDirectory(rootLocation)

            Files.deleteIfExists(rootLocation.resolve("${user.id}.png"))
            Files.copy(file.inputStream, rootLocation.resolve("${user.id}.png"))

            user.photoUrl = "uploads/${user.id}.png"
            userDao.save(user)
        }

    companion object {
        /**
         * Корневая папка для хранения изображений.
         */
        private val rootLocation: Path = Paths.get("uploads")
    }
}