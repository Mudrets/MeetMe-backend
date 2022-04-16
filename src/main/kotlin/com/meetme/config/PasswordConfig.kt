package com.meetme.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

/**
 * Предоставляет алгоритм для шифровки пароля при сохранении его в базу данных.
 */
@Configuration
class PasswordConfig {

    /**
     * Предоставляет класс, шифрующий пароль пользователя с использованием алгоритма BCrypt.
     */
    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder =
        BCryptPasswordEncoder()

}
