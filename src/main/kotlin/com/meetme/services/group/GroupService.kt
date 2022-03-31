package com.meetme.services.group

import com.meetme.services.auth.UserDao
import com.meetme.doIfExist
import com.meetme.domain.dto.goup.CreateGroupDto
import com.meetme.domain.dto.goup.EditGroupDto
import com.meetme.domain.dto.meeting.SearchQuery
import com.meetme.domain.filter.InterestsFilter
import com.meetme.domain.filter.NameFilter
import com.meetme.domain.filter.FilterType
import com.meetme.services.auth.User
import com.meetme.services.file.FileStoreService
import com.meetme.services.invitation.group.InvitationGroupToMeeting
import com.meetme.services.invitation.group.InvitationGroupToMeetingService
import com.meetme.services.iterest.InterestService
import com.meetme.services.medialink.MediaLinkService
import com.meetme.services.meeting.Meeting
import com.meetme.services.meeting.MeetingDao
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

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
    private lateinit var fileStoreService: FileStoreService

    @Autowired
    private lateinit var nameFilter: NameFilter

    @Autowired
    private lateinit var interestsFilter: InterestsFilter

    private fun Iterable<Group>.filter(searchQuery: SearchQuery) =
        this
            .asSequence()
            .filter { nameFilter(it, searchQuery.searchQuery) }
            .filter { interestsFilter(it, searchQuery.interests) }
            .sortedBy(Group::name)
            .toList()

    fun createGroup(createGroupDto: CreateGroupDto): Group =
        createGroupDto.adminId.doIfExist(userDao, logger) { admin ->
            val interestsSet =
                interestService.convertToInterestEntityAndAddNewInterests(interests = createGroupDto.interests)

            val group = Group(
                name = createGroupDto.name,
                description = createGroupDto.description,
                interests = interestsSet,
                admin = admin,
                private = createGroupDto.isPrivate,
            )

            group.socialMediaLinks = mediaLinkService.createNewLinks(createGroupDto.socialMediaLinks, group)

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
            invitationGroupToMeetingService.removeAllGroupInvitation(group)
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
            group.meetings.add(meeting)
            groupDao.save(group)
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

    fun search(userId: Long, searchQuery: SearchQuery): Map<FilterType, List<Group>> =
        userId.doIfExist(userDao, logger) { user ->
            val resMap = mutableMapOf<FilterType, List<Group>>()
            val userGroups = user
                .groups
                .union(user.managedGroup)

            resMap[FilterType.GLOBAL_FILTER] = groupDao.findAllByPrivate(false)
                .subtract(userGroups)
                .filter(searchQuery)

            resMap[FilterType.MY_FILTER] = userGroups
                .filter(searchQuery)

            resMap
        }

    fun getGroupsForUser(userId: Long): List<Group> =
        userId.doIfExist(userDao, logger) { user ->
            user.groups.union(user.managedGroup).toList()
        }

    fun editGroup(groupId: Long, editCredentials: EditGroupDto): Group =
        groupId.doIfExist(groupDao, logger) { group ->
            val interestsSet =
                interestService.convertToInterestEntityAndAddNewInterests(interests = editCredentials.interests)

            val linksSet =
                mediaLinkService.createNewLinks(editCredentials.socialMediaLinks, group)

            group.apply {
                name = editCredentials.name
                description = editCredentials.description
                photoUrl = editCredentials.photoUrl
                private = editCredentials.isPrivate
                interests = interestsSet
                socialMediaLinks = linksSet
            }

            groupDao.save(group)
        }

    fun getMeetings(groupId: Long): List<Meeting> =
        groupId.doIfExist(groupDao, logger) { group -> group.meetings }

    fun sendInvitationToGroups(groupsIds: List<Long>, meetingId: Long) {
        (meetingId to groupsIds).doIfExist(meetingDao, groupDao, logger) { meeting, group ->
            invitationGroupToMeetingService.sendInvitationToGroup(
                group = group,
                meeting = meeting,
            )
        }
    }

    fun uploadImage(file: MultipartFile, groupId: Long): Group =
        groupId.doIfExist(groupDao, logger) { group ->
            val imageUrl = fileStoreService.store(file, group::class.java, group.id)
            group.photoUrl = imageUrl
            groupDao.save(group)
        }
}