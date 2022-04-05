package com.meetme.participants.base

import com.meetme.domain.entity.ParticipantsContainer
import com.meetme.meeting.db.Meeting
import com.meetme.user.db.User

interface ParticipantsService<T : ParticipantsContainer> {

    fun addParticipant(userId: Long, participantContainerId: Long): T

    fun addParticipants(userIds: List<Long>, participantContainerId: Long): T

    fun removeParticipant(userId: Long, participantContainerId: Long): T

    fun getParticipants(participantContainerId: Long): List<User>
}