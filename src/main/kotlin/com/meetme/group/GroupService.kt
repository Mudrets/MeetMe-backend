package com.meetme.group

import com.meetme.auth.User
import com.meetme.auth.UserDao
import com.meetme.doIfExist
import com.meetme.dto.goup.CreateGroupDto
import com.meetme.dto.goup.EditGroupDto
import com.meetme.dto.meeting.CreateMeetingDto
import com.meetme.group.post.Post
import com.meetme.group.post.PostDao
import com.meetme.invitation.group.InvitationGroupToMeeting
import com.meetme.invitation.group.InvitationGroupToMeetingService
import com.meetme.iterest.InterestService
import com.meetme.medialink.MediaLinkService
import com.meetme.meeting.Meeting
import com.meetme.meeting.MeetingDao
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.text.SimpleDateFormat
import java.util.*

@Service
class GroupService {

    private val logger: Logger = LoggerFactory.getLogger(GroupService::class.java)

    @Autowired
    private lateinit var groupDao: GroupDao

    @Autowired
    private lateinit var meetingDao: MeetingDao

    @Autowired
    private lateinit var userDao: UserDao

    @Autowired
    private lateinit var interestService: InterestService

    @Autowired
    private lateinit var mediaLinkService: MediaLinkService

    @Autowired
    private lateinit var invitationGroupToMeetingService: InvitationGroupToMeetingService

    @Autowired
    private lateinit var postDao: PostDao

    fun createGroup(createGroupDto: CreateGroupDto): Group =
        createGroupDto.adminId.doIfExist(userDao, logger) { admin ->
            val interestsSet =
                interestService.convertToInterestEntityAndAddNewInterests(interests = createGroupDto.interests)

            val linksSet =
                mediaLinkService.createNewLinks(links = createGroupDto.links)

            val group = Group(
                name = createGroupDto.name,
                description = createGroupDto.description,
                interests = interestsSet,
                socialMediaLinks = linksSet,
                admin = admin,
                isPrivate = createGroupDto.isPrivate,
            )
            groupDao.save(group)
        }

    private fun getPostTitle(meeting: Meeting): String =
        POST_TITLE.format(meeting.name)

    private fun getLocation(meeting: Meeting): String =
        if (meeting.isOnline)
            "онлайн"
        else
            meeting.location ?: "в неизвестном на данный момент месте"

    private fun getPostText(meeting: Meeting): String {
        val location = getLocation(meeting)
        return POST_TEXT.format(meeting.startDate, meeting.startDate, location)
    }

    private fun addPost(group: Group, meeting: Meeting) {
        val newPost = Post(
            title = getPostTitle(meeting),
            text = getPostText(meeting),
            group = group,
        )
        postDao.save(newPost)
        group.posts.add(newPost)
        groupDao.save(group)
    }

    fun getGroup(groupId: Long): Group =
        groupId.doIfExist(groupDao, logger) { group -> group }

    fun deleteGroup(groupId: Long, userId: Long) =
        (groupId to userId).doIfExist(groupDao, userDao, logger) { group, user ->
            if (group.admin != user)
                throw IllegalArgumentException(
                    "User with id = $userId does not have permission to delete the group with id = $groupId"
                )

            user.managedGroup.remove(group)
            userDao.save(user)
            groupDao.delete(group)
        }

    fun sendInvitationToGroup(groupId: Long, meetingId: Long): InvitationGroupToMeeting =
        (groupId to meetingId).doIfExist(groupDao, meetingDao, logger) { group, meeting ->
            invitationGroupToMeetingService.sendInvitationToGroup(
                group = group,
                meeting = meeting,
            )
        }

    fun acceptInvitation(groupId: Long, meetingId: Long): InvitationGroupToMeeting =
        (groupId to meetingId).doIfExist(groupDao, meetingDao, logger) { group, meeting ->
            val invitation = invitationGroupToMeetingService.acceptInvitation(
                group = group,
                meeting = meeting,
            )
            addPost(group, meeting)
            invitation
        }

    fun cancelInvitation(groupId: Long, meetingId: Long): InvitationGroupToMeeting =
        (groupId to meetingId).doIfExist(groupDao, meetingDao, logger) { group, meeting ->
            invitationGroupToMeetingService.cancelInvitation(
                group = group,
                meeting = meeting,
            )
        }

    fun addParticipantToGroup(groupId: Long, userId: Long): Group =
        (groupId to userId).doIfExist(groupDao, userDao, logger) { group, user ->
            if (group.participants.contains(user))
                throw IllegalArgumentException(
                    "User with id = ${user.id} already is participant of group with id = ${group.id}"
                )

            user.groups.add(group)
            group.participants.add(user)
            groupDao.save(group)
            userDao.save(user)
            group
        }

    fun addParticipantsToGroup(groupId: Long, usersIds: List<Long>): Group =
        groupId.doIfExist(groupDao, logger) { group ->
            for (userId in usersIds)
                addParticipantToGroup(groupId, userId)
            return group
        }

    fun getParticipants(groupId: Long): List<User> =
        groupId.doIfExist(groupDao, logger, Group::participants)

    fun deleteUser(groupId: Long, userId: Long): Group =
        (groupId to userId).doIfExist(groupDao, userDao, logger) { group, user ->
            if (!group.participants.contains(user))
                throw IllegalArgumentException(
                    "The user with id = $userId is not a member of the meeting $groupId"
                )
            group.participants.remove(user)
            user.groups.remove(group)
            groupDao.save(group)
            userDao.save(user)
            group
        }

    fun searchGroups(search: String): List<Group> =
        groupDao.findAll()
            .filter { it.name.contains(search) }

    fun getGroupsForUser(userId: Long): List<Group> =
        userId.doIfExist(userDao, logger) { user ->
            user.groups.union(user.managedGroup).toList()
        }

    fun editGroup(groupId: Long, editCredentials: EditGroupDto): Group =
        groupId.doIfExist(groupDao, logger) { group ->
            val interestsSet =
                interestService.convertToInterestEntityAndAddNewInterests(interests = editCredentials.interests)

            val linksSet =
                mediaLinkService.createNewLinks(links = editCredentials.socialMediaLinks)

            group.apply {
                name = editCredentials.name
                description = editCredentials.description
                photoUrl = editCredentials.photoUrl
                isPrivate = editCredentials.isPrivate
                interests = interestsSet
                socialMediaLinks = linksSet
            }

            groupDao.save(group)
        }

    companion object {
        private const val POST_TITLE = "Мероприятие %s"

        private const val POST_TEXT =
            "Группа была приглашена на мероприятие %s, проходящее %s. Мероприятие будет проводиться %s"
    }
}