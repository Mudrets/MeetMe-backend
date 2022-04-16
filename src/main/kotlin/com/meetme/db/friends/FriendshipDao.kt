package com.meetme.db.friends

import com.meetme.db.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Интерфейс предоставляющий абстракцию для работы с таблицей дружеских отношений в базе данных.
 */
@Repository("friendshipRepository")
interface FriendshipDao : JpaRepository<Friendship, Long> {
    fun findAllByUser1OrUser2(user1: User, user2: User): List<Friendship>

    fun findByUser1AndUser2(user1: User, user2: User): Friendship?
}