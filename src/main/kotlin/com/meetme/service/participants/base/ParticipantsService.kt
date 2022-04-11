package com.meetme.service.participants.base

import com.meetme.domain.entity.ParticipantsContainer
import com.meetme.db.user.User

interface ParticipantsService<T : ParticipantsContainer> {

    fun addParticipant(userId: Long, participantContainerId: Long): T

    fun addParticipants(userIds: List<Long>, participantContainerId: Long): T

    fun removeParticipant(userId: Long, participantContainerId: Long): T

    fun getParticipants(participantContainerId: Long): List<User>
}