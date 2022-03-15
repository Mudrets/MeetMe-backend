package com.meetme.auth

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService : UserDetailsService {

    private val logger: Logger = LoggerFactory.getLogger(UserService::class.java)

    @Autowired
    private val userDao: UserDao? = null

    @Autowired
    private val bCryptPasswordEncoder: BCryptPasswordEncoder? = null

    fun createNewUserByEmailAndPass(email: String, password: String): User? {
        if (bCryptPasswordEncoder != null && userDao != null) {
            if (loadUserByUsername(email) != null) return null

            val newUser = userDao.save(
                User(
                    email = email,
                    password = bCryptPasswordEncoder.encode(password)
                )
            )

            logger.debug("User $newUser created")
            return newUser
        }
        return null
    }

    override fun loadUserByUsername(email: String): UserDetails? {
        val dbUser = userDao?.findByEmail(email)

        if (dbUser != null)
            logger.debug("User $dbUser found by email: $email")
        else
            logger.debug("User not found by email: $email")

        return dbUser
    }

}