package com.meetme.service.user

import com.meetme.domain.CrudService
import com.meetme.domain.dto.auth.RegisterCredentialsDto
import com.meetme.domain.dto.user.EditUserDto
import com.meetme.db.user.User
import org.springframework.security.core.userdetails.UserDetailsService

/**
 * Сервис для работы с пользователем.
 */
interface UserService :
    CrudService<RegisterCredentialsDto, EditUserDto, Long, User>, UserDetailsService {

    /**
     * Вход в аккаун пользователя с помщью электронной почты и пароля.
     * @param email электронная почта.
     * @param password пароль.
     * @return Возвращается пользователь с переданной электронной почтой
     * и паролем.
     */
    fun loginUser(email: String, password: String): User

    /**
     * Проверяет отправленный код и если он совпадает с кодом подтверждения аккаунта
     * то аккаунт пользователя активируется.
     * @param code код подтверждения почты.
     * @param userId идентификатор пользователя.
     * @return Возвращает данные о пользователе.
     */
    fun verifyAccount(code: String, userId: Long): User

    /**
     * Повторно отправляет код активации аккаунта на почту.
     * @param userId электронная почта для отправки кода.
     */
    fun sendNewAccountCode(userId: Long)
}