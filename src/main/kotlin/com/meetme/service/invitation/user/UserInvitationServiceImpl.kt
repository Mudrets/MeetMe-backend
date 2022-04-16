package com.meetme.service.invitation.user

import com.meetme.service.invitation.BaseInvitationService
import com.meetme.db.invitation.Invitation
import com.meetme.db.invitation.InvitationDao
import com.meetme.db.meeting.Meeting
import com.meetme.service.meeting.MeetingService
import com.meetme.service.participants.base.ParticipantsService
import com.meetme.service.user.UserService
import com.meetme.service.user.UserServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

/**
 * Реализация сервиса для работы с приглашениями пользователей на мероприятия.
 */
@Service("userInvitationService")
class UserInvitationServiceImpl @Autowired constructor(
    invitationDao: InvitationDao,
    meetingService: MeetingService,
    /**
     * Сервис для работы с пользователем.
     */
    private val userService: UserService,
    @Qualifier("meetingParticipantsService")
    /**
     * Сервис для работы с учатниками мероприятия.
     */
    private val meetingParticipantsService: ParticipantsService<Meeting>,
) : BaseInvitationService(invitationDao, meetingService) {
    /**
     * Добавляет список пользователй в приглашение на некоторое мероприятие.
     * @param ids список идентификаторов добавляемых пользователей.
     * @param invitation приглашение, в которое добавляются пользователи.
     * @return Возвращает список идентификаторов пользователей, для которых необходимо сразу принять
     * приглашение.
     */
    override fun addInInvitation(ids: List<Long>, invitation: Invitation): List<Long> {
        val users = userService.getList(ids)
            .filter { user -> !user.allMeetings.contains(invitation.meeting) }
        invitation.users.addAll(users)

        return users
            .filter { user -> user == invitation.meeting.admin }
            .map { user -> user.id }
    }

    /**
     * Добавляет мероприятияе в пользователя с переданным идентификатором.
     * @param id идентификатор пользователя.
     * @param invitation приглашение на мероприятие, мероприятие которого необходимо добавить в пользователя.
     */
    override fun addMeeting(id: Long, invitation: Invitation) {
        val user = checkUser(id, invitation)
        meetingParticipantsService.addParticipant(user.id, invitation.meeting.id)
        invitation.users.remove(user)
        userService.save(user)
    }

    /**
     * Удаляет пользователя из приглашения на мероприятяе.
     * @param id идентификатор удаляемого пользователя.
     * @param invitation приглашение на мероприятие.
     */
    override fun deleteFromInvitation(id: Long, invitation: Invitation) {
        val user = checkUser(id, invitation)
        user.invitations.remove(invitation)
        invitation.users.remove(user)
        userService.save(user)
    }

    /**
     * Проверяет есть ли пользователь с переданным идентификатором в приглашении.
     * @param userId идентификатор проверяемого пользователя.
     * @param invitation приглашение на мероприятие.
     * @return Возваращет True, если пользователь содержится в приглашении и False в
     * противном случае.
     */
    private fun checkUser(userId: Long, invitation: Invitation) =
        invitation.users.firstOrNull { user -> user.id == userId }
            ?: throw IllegalArgumentException(
                "User with id = $userId don't have invitation on meeting with id = ${invitation.meeting.id}"
            )
}