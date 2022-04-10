package com.meetme.participants.base

import com.meetme.doIfExist
import com.meetme.domain.CrudService
import com.meetme.domain.EntityGetter
import com.meetme.domain.StoreService
import com.meetme.domain.entity.ParticipantsContainer
import com.meetme.user.UserService
import com.meetme.user.UserServiceImpl
import com.meetme.user.db.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

abstract class ParticipantsBaseService<T : ParticipantsContainer>(
    private val userService: UserService,
    private val service: StoreService<T, Long>,
) : ParticipantsService<T> {

    abstract fun checkEntityBeforeAdd(entity: T, user: User)

    abstract fun checkEntityBeforeRemove(entity: T, user: User)

    abstract fun addContainerToUser(container: T, user: User)

    abstract fun removeContainerFromUser(container: T, user: User)

    private fun getContainer(participantContainerId: Long): T {
        return service.get(participantContainerId)
    }

    final override fun addParticipant(userId: Long, participantContainerId: Long): T =
        userId.doIfExist(userService) { user ->
            val container = getContainer(participantContainerId)
            checkEntityBeforeAdd(container, user)
            addContainerToUser(container, user)
            container.participants.add(user)
            service.save(container)
        }

    final override fun addParticipants(userIds: List<Long>, participantContainerId: Long): T {
        val users = userService.getList(userIds)
        val container = getContainer(participantContainerId)
        users.forEach { user ->
            checkEntityBeforeAdd(container, user)
            addContainerToUser(container, user)
        }
        container.participants.addAll(users)
        return service.save(container)
    }

    final override fun removeParticipant(userId: Long, participantContainerId: Long) =
        userId.doIfExist(userService) { user ->
            val container = getContainer(participantContainerId)
            checkEntityBeforeRemove(container, user)
            container.participants.remove(user)
            removeContainerFromUser(container, user)
            service.save(container)
        }

    final override fun getParticipants(participantContainerId: Long): List<User> {
        val container = getContainer(participantContainerId)
        return container.participants
    }
}