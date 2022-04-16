package com.meetme.service.invitation

import com.meetme.util.doIfExist
import com.meetme.db.invitation.Invitation
import com.meetme.db.invitation.InvitationDao
import com.meetme.service.meeting.MeetingService
import com.meetme.service.meeting.MeetingServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Базовая реализация сервиса для работы с приглашениями на мероприятия.
 */
@Service
abstract class BaseInvitationService(
    /**
     * Data access object для работы с данными в таблице приглашений в базе данных.
     */
    private val invitationDao: InvitationDao,
    /**
     * Сервис для работы с мероприятиями.
     */
    private val meetingService: MeetingService,
) : InvitationService {
    /**
     * Добавляет список определенных сущностей в приглашение на некоторое мероприятие.
     * @param ids список идентификаторов добавляемых сущностей.
     * @param invitation приглашение, в которое добавляются сущности.
     * @return Возвращает список идентификаторов сущностей, для которых необходимо сразу принять
     * приглашение.
     */
    abstract fun addInInvitation(ids: List<Long>, invitation: Invitation): List<Long>

    /**
     * Добавляет мероприятияе в сущность с переданным идентификатором.
     * @param id идентификатор сущности.
     * @param invitation приглашение на мероприятие, мероприятие которого необходимо добавить в сущность.
     */
    abstract fun addMeeting(id: Long, invitation: Invitation)

    /**
     * Удаляет сущность из приглашения на мероприятяе.
     * @param id идентификатор удаляемой сущности.
     * @param invitation приглашение на мероприятие.
     */
    abstract fun deleteFromInvitation(id: Long, invitation: Invitation)

    /**
     * Отправляет приглашение на мероприятие с переданным идентификатором всем сущностям с идентификатарми
     * переданными в списке.
     * @param ids список идентификаторов приглашаемых на мероприятие сущностей.
     * @param meetingId идентификатор мероприятия, на которое приглашено мероприятие.
     */
    final override fun sendInvitations(ids: List<Long>, meetingId: Long) {
        meetingId.doIfExist(meetingService) { meeting ->
            synchronized(lock) {
                val invitation =
                    invitationDao.findByMeeting(meetingId) ?: Invitation(meeting = meeting)
                val instantAcceptList = addInInvitation(ids, invitation)
                invitationDao.save(invitation)
                instantAcceptList.forEach { id -> acceptInvitation(id, meetingId) }
            }
        }
    }

    /**
     * Принимает приглашение на мероприятие с переданным идентификатором для сущности с переданным
     * идентификатором.
     * @param id идентификатор сущности.
     * @param meetingId идентификатор мепроприятия.
     */
    final override fun acceptInvitation(id: Long, meetingId: Long) {
        val invitation = getInvitation(meetingId)
        addMeeting(id, invitation)
        invitationDao.save(invitation)
    }

    /**
     * Отклоняет приглашение на мероприятие с переданным идентификатором для сущности с переданным
     * идентификатором.
     * @param id идентификатор сущности.
     * @param meetingId идентификатор мепроприятия.
     */
    final override fun cancelInvitation(id: Long, meetingId: Long) {
        val invitation = getInvitation(meetingId)
        deleteFromInvitation(id, invitation)
        invitationDao.save(invitation)
    }

    /**
     * Получает приглашение на мероприятие с переданным идентификатором, если приглашение существует,
     * в противном случае создает его.
     * @param meetingId идентификатор мероприятия.
     * @return Возвращает полученное или созданное приглашение на мероприятие.
     */
    private fun getInvitation(meetingId: Long): Invitation =
        invitationDao.findByMeeting(meetingId)
            ?: throw IllegalArgumentException("Invitation on the meeting with id $meetingId does not exist")

    companion object {
        val lock = Any()
    }
}