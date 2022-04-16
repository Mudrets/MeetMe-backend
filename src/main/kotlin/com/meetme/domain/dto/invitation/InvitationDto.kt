package com.meetme.domain.dto.invitation

/**
 * Data transfer object для отправления приглашений
 * на мероприятие группам и пользователем
 */
class InvitationDto(
    /**
     * Список идентификаторов приглашенных пользователей.
     */
    val users: List<Long> = listOf(),
    /**
     * Список идентификаторов приглашенных групп.
     */
    val groups: List<Long> = listOf(),
)