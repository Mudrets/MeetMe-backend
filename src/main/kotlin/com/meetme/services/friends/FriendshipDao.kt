package com.meetme.services.friends

import com.meetme.services.auth.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository("friendshipRepository")
interface FriendshipDao : JpaRepository<Friendship, Long> {
    fun findAllByUser1OrUser2(user1: User, user2: User): List<Friendship>?

    fun findByUser1AndUser2(user1: User, user2: User): Friendship?
}