package com.meetme.config

import com.meetme.auth.User
import com.meetme.auth.UserDao
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UserConfig {

    @Bean
    fun commandLineRunner(userDao: UserDao) = CommandLineRunner {
            val alex = User(email = "alex", password = "123456")
            val mariam = User(email = "mariam", password = "123456")

            userDao.saveAll(
                    listOf(alex, mariam)
            )
        }
}