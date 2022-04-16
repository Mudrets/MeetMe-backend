package com.meetme.service.participants.base

import com.meetme.util.doIfExist
import com.meetme.domain.StoreService
import com.meetme.domain.entity.ParticipantsContainer
import com.meetme.service.user.UserService
import com.meetme.db.user.User

/**
 * Базовая реализация сервиса для работы с участниками некоторой сущности.
 * @param T сущность, содержащая в себе участников.
 */
abstract class ParticipantsBaseService<T : ParticipantsContainer>(
    private val userService: UserService,
    private val service: StoreService<T, Long>,
) : ParticipantsService<T> {

    /**
     * Проверяет сущность перед тем как добавить в нее пользователя.
     * @param entity сущность, хранящая пользователей.
     * @param user пользователь.
     */
    abstract fun checkEntityBeforeAdd(entity: T, user: User)

    /**
     * Проверяет сущность перед тем как удалить из нее пользователя.
     * @param entity сущность, хранящая пользователей.
     * @param user пользователь.
     */
    abstract fun checkEntityBeforeRemove(entity: T, user: User)

    /**
     * Добавляет пользователя в сущность, хранящую пользователей.
     * @param container сущность, хранящая пользователей.
     * @param user пользователь.
     */
    abstract fun addContainerToUser(container: T, user: User)

    /**
     * Удаляет пользователя из сущности, хранящую пользователей.
     * @param container сущность, хранящая пользователей.
     * @param user пользователь.
     */
    abstract fun removeContainerFromUser(container: T, user: User)

    /**
     * Получает сущность, хранящую пользователей по переданному идентификатору.
     * @param participantContainerId идентификатор мероприятия.
     * @return Возвращает найденную по индентификатору сущность.
     */
    private fun getContainer(participantContainerId: Long): T {
        return service.get(participantContainerId)
    }

    /**
     * Добавляет пользователя в качестве участника в сущность.
     * @param userId идентификатор пользователя.
     * @param participantContainerId идентификатор сущности, храняшей пользователей.
     * @return Возвращает сущность, содержащую пользователей после добавления нового
     * участника.
     */
    final override fun addParticipant(userId: Long, participantContainerId: Long): T =
        userId.doIfExist(userService) { user ->
            val container = getContainer(participantContainerId)
            checkEntityBeforeAdd(container, user)
            addContainerToUser(container, user)
            container.participants.add(user)
            service.save(container)
        }


    /**
     * Добавляет пользователей в качестве участников в сущность.
     * @param userIds список идентификаторов пользователей, добавляемых в сущность.
     * @param participantContainerId идентификатор сущности, содержащей пользователей.
     * @return Возвращает сущность, содержащую пользователей после добавления новых
     * участников.
     */
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

    /**
     * Удаляет пользователя в качестве участника в сущность.
     * @param userId идентификатор пользователя.
     * @param participantContainerId идентификатор сущности, храняшей пользователей.
     */
    final override fun removeParticipant(userId: Long, participantContainerId: Long) =
        userId.doIfExist(userService) { user ->
            val container = getContainer(participantContainerId)
            checkEntityBeforeRemove(container, user)
            container.participants.remove(user)
            removeContainerFromUser(container, user)
            service.save(container)
        }

    /**
     * Возвращает участников сущности.
     * @param participantContainerId идентификатор сущности, содержащей пользователей.
     */
    final override fun getParticipants(participantContainerId: Long): List<User> {
        val container = getContainer(participantContainerId)
        return container.participants
    }
}