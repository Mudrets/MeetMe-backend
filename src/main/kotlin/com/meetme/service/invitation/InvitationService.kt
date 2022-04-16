package com.meetme.service.invitation

/**
 * Сервис для работы с приглашениями.
 */
interface InvitationService {

    /**
     * Отправляет приглашение на мероприятие с переданным идентификатором всем сущностям с идентификатарми
     * переданными в списке.
     * @param ids список идентификаторов приглашаемых на мероприятие сущностей.
     * @param meetingId идентификатор мероприятия, на которое приглашено мероприятие.
     */
    fun sendInvitations(ids: List<Long>, meetingId: Long)

    /**
     * Принимает приглашение на мероприятие с переданным идентификатором для сущности с переданным
     * идентификатором.
     * @param id идентификатор сущности.
     * @param meetingId идентификатор мепроприятия.
     */
    fun acceptInvitation(id: Long, meetingId: Long)

    /**
     * Отклоняет приглашение на мероприятие с переданным идентификатором для сущности с переданным
     * идентификатором.
     * @param id идентификатор сущности.
     * @param meetingId идентификатор мепроприятия.
     */
    fun cancelInvitation(id: Long, meetingId: Long)
}