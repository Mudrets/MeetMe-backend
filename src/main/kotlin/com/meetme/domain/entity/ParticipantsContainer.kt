package com.meetme.domain.entity

import com.meetme.db.user.User

interface ParticipantsContainer {

    val participants: MutableList<User>
}