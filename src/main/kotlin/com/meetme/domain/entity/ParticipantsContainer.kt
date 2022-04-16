package com.meetme.domain.entity

import com.meetme.db.user.User

/**
 * Интерфейс контейнера для участников
 */
interface ParticipantsContainer {
    /**
     * Участники
     */
    val participants: MutableList<User>
}