package com.meetme.auth

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service


@Service
class UserService {

    @Autowired
    private val userDao: UserDao? = null

    @Autowired
    private val bCryptPasswordEncoder: BCryptPasswordEncoder? = null

    fun getUsers(): MutableList<User?>? = userDao?.findAll()

    fun createNewUser(username: String, password: String): User? {
        if (bCryptPasswordEncoder != null && userDao != null)
            return userDao.save(
                User(
                    username = username,
                    password = bCryptPasswordEncoder.encode(password)
                )
            )
        return null
    }

}