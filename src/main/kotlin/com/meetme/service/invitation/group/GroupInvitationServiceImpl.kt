package com.meetme.service.invitation.group

import com.meetme.service.group.GroupServiceImpl
import com.meetme.service.invitation.BaseInvitationService
import com.meetme.db.invitation.Invitation
import com.meetme.db.group.Post
import com.meetme.db.invitation.InvitationDao
import com.meetme.service.group.GroupService
import com.meetme.service.invitation.InvitationService
import com.meetme.service.meeting.MeetingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Реализация сервиса для работы с приглашениями групп на мероприятия.
 */
@Service("groupInvitationService")
class GroupInvitationServiceImpl @Autowired constructor(
    invitationDao: InvitationDao,
    meetingService: MeetingService,
    /**
     * Сервис для работы с группами.
     */
    private val groupService: GroupService,
) : BaseInvitationService(invitationDao, meetingService) {
    /**
     * Добавляет список групп в приглашение на некоторое мероприятие.
     * @param ids список идентификаторов добавляемых групп.
     * @param invitation приглашение, в которое добавляются сущности.
     * @return Возвращает список идентификаторов групп, для которых необходимо сразу принять
     * приглашение.
     */
    override fun addInInvitation(ids: List<Long>, invitation: Invitation): List<Long> {
        val groups = groupService.getList(ids)
            .filter { group -> !group.containsMeeting(invitation.meeting) }
        invitation.groups.addAll(groups)
        return groups
            .filter { group -> group.admin == invitation.meeting.admin }
            .map { it.id }
    }

    /**
     * Добавляет мероприятияе в группу с переданным идентификатором.
     * @param id идентификатор группы.
     * @param invitation приглашение, мероприятие которого необходимо добавить в сущность.
     */
    override fun addMeeting(id: Long, invitation: Invitation) {
        val group = checkGroup(id, invitation)
        val usersWithoutInvitation = group.participants
            .filter { user -> !user.meetings.contains(invitation.meeting) }

        group.posts.add(Post(meeting = invitation.meeting, group = group))
        invitation.users.addAll(usersWithoutInvitation)
        invitation.groups.remove(group)
    }

    /**
     * Удаляет группу из приглашения на мероприятяе.
     * @param id идентификатор удаляемой группы.
     * @param invitation приглашение на мероприятие.
     */
    override fun deleteFromInvitation(id: Long, invitation: Invitation) {
        val group = checkGroup(id, invitation)
        invitation.groups.remove(group)
    }

    /**
     * Проверяет есть ли группа с переданным идентификатором в приглашении.
     * @param groupId идентификатор проверяемой группы.
     * @param invitation приглашение на мероприятие.
     * @return Возваращет True, если группа содержится в приглашении и False в
     * противном случае.
     */
    private fun checkGroup(groupId: Long, invitation: Invitation) =
        invitation.groups.firstOrNull { group -> group.id == groupId }
            ?: throw IllegalArgumentException(
                "Group with id = $groupId don't have invitation on meeting with id = ${invitation.meeting.id}"
            )
}