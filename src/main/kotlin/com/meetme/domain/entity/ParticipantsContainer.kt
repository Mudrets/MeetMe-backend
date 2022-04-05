package com.meetme.domain.entity

import com.meetme.user.db.User

interface ParticipantsContainer {

    val participants: MutableList<User>
}