package com.meetme.service.user

import com.meetme.domain.dto.user.EditUserDto
import com.meetme.util.doIfExist
import com.meetme.domain.dto.auth.RegisterCredentialsDto
import com.meetme.service.interest.InterestService
import com.meetme.service.media_link.MediaLinkService
import com.meetme.db.user.User
import com.meetme.db.user.UserDao
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

/**
 * Реализация сервиса для работы с пользователем.
 */
@Service
class UserServiceImpl @Autowired constructor(
    /**
     * Data access object предоставляющий доступ к таблице пользователей в базе данных.
     */
    private val userDao: UserDao,
    /**
     * Шифровщик для пароля.
     */
    private val passwordEncoder: PasswordEncoder,
    /**
     * Сервис для работы с интересами.
     */
    private val interestService: InterestService,
    /**
     * Сервис для работы с ссылками на социальные сети.
     */
    private val mediaLinkService: MediaLinkService,
): UserService {

    /**
     * Логгер для логгирования
     */
    private val logger: Logger = LoggerFactory.getLogger(UserServiceImpl::class.java)

    override fun loadUserByUsername(username: String): UserDetails? = loadUserByEmail(username)

    /**
     * Создает пользователя по переданным данным.
     * @param data данные для создания пользователя.
     * @return Возращает созданного пользователя.
     */
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

    /**
     * Изменяет пользователя в соответствии с переданными данными.
     * @param identifier идентификатор пользователя.
     * @param data данные для изменения пользователя.
     * @return Возвращает измененного пользователя.
     */
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

    /**
     * Получает пользователя по переданному идентификатору.
     * @param identifier идентификатор.
     * @return Возвращает полученного пользователя.
     */
    override fun get(identifier: Long): User =
        identifier.doIfExist(userDao, logger) { it }

    /**
     * Получает всех существующих пользователей.
     * @return Возвращает список всех пользователей.
     */
    override fun getAll(): List<User> = userDao.findAll()

    /**
     * Сохроняет пользователя в хранилище.
     * @param entity сохраняемая пользователя.
     * @return Возвращает сохраненного пользователя.
     */
    override fun save(entity: User) = userDao.save(entity)

    /**
     * Удаляет переданную пользователя.
     * @param entity удаляемый пользователя.
     */
    override fun delete(entity: User) = userDao.delete(entity)

    /**
     * Удаляет пользователя по переданному идентификатору.
     * @param identifier идентификатор удаляемого пользователя.
     */
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