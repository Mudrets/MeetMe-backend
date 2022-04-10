package com.meetme.user

import com.meetme.domain.dto.user.EditUserDto
import com.meetme.doIfExist
import com.meetme.domain.dto.auth.RegisterCredentialsDto
import com.meetme.interest.InterestService
import com.meetme.media_link.MediaLinkService
import com.meetme.user.db.User
import com.meetme.user.db.UserDao
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl : UserService {

    private val logger: Logger = LoggerFactory.getLogger(UserServiceImpl::class.java)

    @Autowired
    private lateinit var userDao: UserDao

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var interestService: InterestService

    @Autowired
    private lateinit var mediaLinkService: MediaLinkService

    override fun loadUserByUsername(username: String): UserDetails? = loadUserByEmail(username)

    override fun create(data: RegisterCredentialsDto): User {
        if (userDao.findByEmail(data.email) != null)
            throw IllegalArgumentException("User with email ${data.email} already exists")

        val newUser = userDao.save(
            User(
                email = data.email,
                password = passwordEncoder.encode(data.password),
                name = getName(data.fullName),
                surname = getSurname(data.fullName),
            )
        )

        logger.debug("User $newUser created")
        return newUser
    }

    override fun loginUser(email: String, password: String): User {
        val user = loadUserByEmail(email)
        if (!checkPassword(user, password))
            throw IllegalArgumentException("Incorrect password")
        return user
    }

    override fun update(identifier: Long, data: EditUserDto): User =
        identifier.doIfExist(userDao, logger) { user ->
            val interestsSet =
                interestService.convertToInterestEntityAndAddNewInterests(interests = data.interests)

            val links = mediaLinkService.createNewLinks(data.mediaLinks, user)

            user.apply {
                name = getName(data.fullName)
                surname = getSurname(data.fullName)
                description = data.description
                interests = interestsSet
                socialMediaLinks = links
            }
            userDao.save(user)
        }

    override fun get(identifier: Long): User =
        identifier.doIfExist(userDao, logger) { it }

    override fun getAll(): List<User> = userDao.findAll()

    override fun save(entity: User) = userDao.save(entity)

    override fun delete(entity: User) = userDao.delete(entity)

    override fun deleteByIdentifier(identifier: Long) = userDao.deleteById(identifier)

    private fun getName(fullName: String): String = fullName.split(' ')[0]

    private fun getSurname(fullName: String): String =
        if (fullName.trim().contains(' '))
            fullName.split(' ')[1]
        else
            ""

    private fun checkPassword(user: User, password: String): Boolean =
        passwordEncoder.matches(password, user.password)

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
}