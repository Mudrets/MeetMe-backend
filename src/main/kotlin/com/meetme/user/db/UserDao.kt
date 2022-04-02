package com.meetme.user.db

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository("userRepository")
interface UserDao : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
}