package com.meetme.auth

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository("userRepository")
interface UserDao : JpaRepository<User?, Long?> {

    fun findByUsername(username: String): User
}