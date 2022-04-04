package com.meetme.user

import com.meetme.domain.dto.user.EditUserDto
import com.meetme.doIfExist
import com.meetme.domain.Store
import com.meetme.domain.filter.NameFilter
import com.meetme.friends.db.Friendship
import com.meetme.getEntity
import com.meetme.file.FileStoreService
import com.meetme.friends.FriendshipServiceImpl
import com.meetme.interest.InterestService
import com.meetme.media_link.MediaLinkService
import com.meetme.meeting.mapper.MeetingToMeetingDto
import com.meetme.user.db.User
import com.meetme.user.db.UserDao
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class UserService : UserDetailsService, Store<User> {

    private val logger: Logger = LoggerFactory.getLogger(UserService::class.java)

    @Autowired
    private lateinit var userDao: UserDao

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var interestService: InterestService

    @Autowired
    private lateinit var mediaLinkService: MediaLinkService

    @Autowired
    private lateinit var fileStoreService: FileStoreService

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

    fun changeName(userId: Long, newName: String): User? {
        val dbUser = userId.getEntity(userDao, logger)
        dbUser?.name = newName
        dbUser?.let { userDao.save(it) }
        return dbUser
    }

    fun getUser(userId: Long): User =
        userId.doIfExist(userDao, logger) { user -> user }

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
            val imageUrl = fileStoreService.store(file, user::class.java, user.id)
            user.photoUrl = imageUrl
            userDao.save(user)
        }

    fun getAllUsers(): List<User> = userDao.findAll()

    override fun getEntity(id: Long) = id.getEntity(userDao, logger)

    override fun save(entity: User) {
        userDao.save(entity)
    }
}