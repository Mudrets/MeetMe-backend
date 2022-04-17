package com.meetme.service.user

import com.meetme.domain.dto.DataResponse
import com.meetme.domain.dto.auth.LoginCredentialsDto
import com.meetme.domain.dto.auth.RegisterCredentialsDto
import com.meetme.domain.dto.auth.UserDto
import com.meetme.domain.dto.user.EditUserDto
import com.meetme.util.tryExecute
import com.meetme.db.user.User
import com.meetme.service.user.mapper.UserToUserDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * Контроллер для работы с пользователем
 */
@RestController
@RequestMapping("/api/v1/user")
class UserController @Autowired constructor(
    /**
     * Сервис для работы с пользователем.
     */
    private val userService: UserService,
    /**
     * Маппер, преобразующий User в UserDto.
     */
    private val userToUserDto: UserToUserDto,
) {
    /**
     * Обработчик HTTP POST запроса по url /api/v1/user/register для регистрации нового пользователя.
     * @param credentials данные для регистрации пользователя.
     */
    @PostMapping("/register")
    fun register(@RequestBody credentials: RegisterCredentialsDto): DataResponse<UserDto> =
        tryExecute {
            val user = userService.create(credentials)
            userToUserDto(user)
        }

    /**
     * Обработчик HTTP POST запроса по url /api/v1/user/login" для получения данных о пользователе
     * при входе в аккаунт.
     * @param credentials данные для входа.
     */
    @PostMapping("/login")
    fun login(@RequestBody credentials: LoginCredentialsDto): DataResponse<UserDto> =
        tryExecute {
            val user = userService.loginUser(
                email = credentials.email,
                password = credentials.password,
            )
            userToUserDto(user)
        }

    /**
     * Обработчик HTTP POST запроса по url /api/v1/user/{user_id} для получения пользователя.
     * @param userId идентификатор пользователя
     */
    @GetMapping("/{user_id}")
    fun getUser(@PathVariable("user_id") userId: Long): DataResponse<User> =
        tryExecute {
            userService.get(userId)
        }

    /**
     * Обработчик HTTP POST запроса по url /api/v1/user/{user_id}/edit для редактирования данных
     * о пользователе.
     * @param userId идентификатор пользователя.
     * @param editUserDto данные для редактирования пользователя.
     */
    @PostMapping("/{user_id}/edit")
    fun editUser(
        @PathVariable("user_id") userId: Long,
        @RequestBody editUserDto: EditUserDto,
    ): DataResponse<UserDto> =
        tryExecute {
            userToUserDto(userService.update(userId, editUserDto))
        }

    /**
     * Обработчик HTTP POST запроса по url /api/v1/user/{user_id}/verify/{code} для активации
     * аккаунта пользователя.
     * @param userId идентификатор пользователя.
     * @param code код подтверждения регистрации.
     */
    @PostMapping("{user_id}/verify/{code}")
    fun verifyUser(
        @PathVariable("user_id") userId: Long,
        @PathVariable("code") code: String,
    ): DataResponse<UserDto> =
        tryExecute {
            userToUserDto(userService.verifyAccount(code, userId))
        }

    /**
     * Обработчик HTTP POST запроса по url /api/v1/user/{user_id}/send_new_code для отправления нового
     * кода активации аккаунта на почту.
     * @param userId идентификатор пользователя.
     */
    @PostMapping("{user_id}/send_new_code")
    fun sendNewCode(@PathVariable("user_id") userId: Long): DataResponse<Unit?> =
        tryExecute {
            userService.sendNewAccountCode(userId)
            null
        }
}

