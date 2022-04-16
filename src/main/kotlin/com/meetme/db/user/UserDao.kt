package com.meetme.db.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Интерфейс предоставляющий абстракцию для работы с таблицей пользователей в базе данных.
 */
@Repository("userRepository")
interface UserDao : JpaRepository<User, Long> {
    /**
     * Находит пользователя по переданной электронной почте.
     * @param email электронная почта пользователя.
     * @return пользователь с переданной электронной почтой или null если такого
     * пользователя не существует.
     */
    fun findByEmail(email: String): User?
}