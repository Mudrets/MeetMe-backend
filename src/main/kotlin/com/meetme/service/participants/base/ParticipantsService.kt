package com.meetme.service.participants.base

import com.meetme.domain.entity.ParticipantsContainer
import com.meetme.db.user.User

/**
 * Сервис для работы с участниками некоторой сущности.
 * @param T сущность, содержащая в себе участников.
 */
interface ParticipantsService<T : ParticipantsContainer> {

    /**
     * Добавляет пользователя в качестве участника в сущность.
     * @param userId идентификатор пользователя.
     * @param participantContainerId идентификатор сущности, храняшей пользователей.
     * @return Возвращает сущность, содержащую пользователей после добавления нового
     * участника.
     */
    fun addParticipant(userId: Long, participantContainerId: Long): T

    /**
     * Добавляет пользователей в качестве участников в сущность.
     * @param userIds список идентификаторов пользователей, добавляемых в сущность.
     * @param participantContainerId идентификатор сущности, содержащей пользователей.
     * @return Возвращает сущность, содержащую пользователей после добавления новых
     * участников.
     */
    fun addParticipants(userIds: List<Long>, participantContainerId: Long): T

    /**
     * Удаляет пользователя в качестве участника в сущность.
     * @param userId идентификатор пользователя.
     * @param participantContainerId идентификатор сущности, храняшей пользователей.
     */
    fun removeParticipant(userId: Long, participantContainerId: Long): T

    /**
     * Возвращает участников сущности.
     * @param participantContainerId идентификатор сущности, содержащей пользователей.
     */
    fun getParticipants(participantContainerId: Long): List<User>
}