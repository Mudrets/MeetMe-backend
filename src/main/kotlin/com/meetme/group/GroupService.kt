package com.meetme.group

import com.meetme.user.db.UserDao
import com.meetme.doIfExist
import com.meetme.domain.ListEntityGetter
import com.meetme.domain.dto.goup.CreateGroupDto
import com.meetme.domain.dto.goup.EditGroupDto
import com.meetme.domain.dto.meeting.SearchQuery
import com.meetme.domain.filter.InterestsFilter
import com.meetme.domain.filter.NameFilter
import com.meetme.domain.filter.FilterType
import com.meetme.user.db.User
import com.meetme.meeting.db.Meeting
import com.meetme.meeting.db.MeetingDao
import com.meetme.file.FileStoreService
import com.meetme.getEntity
import com.meetme.group.db.Group
import com.meetme.group.db.GroupDao
import com.meetme.interest.InterestService
import com.meetme.invitation.InvitationService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class GroupService : ListEntityGetter<Group> {

    private val logger: Logger = LoggerFactory.getLogger(GroupService::class.java)

    @Autowired
    private lateinit var groupDao: GroupDao

    @Autowired
    private lateinit var userDao: UserDao

    @Autowired
    private lateinit var interestService: InterestService

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
            group.invitations.forEach { it.groups.remove(group) }
            groupDao.save(group)
            groupDao.delete(group)
        }

    fun addParticipantToGroup(groupId: Long, userId: Long): Group =
        (groupId to userId).doIfExist(groupDao, userDao, logger) { group, user ->
            if (group.participants.contains(user))
                throw IllegalArgumentException(
                    "User with id = ${user.id} already is participant of group with id = ${group.id}"
                )
            group.participants.add(user)
            groupDao.save(group)
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
            groupDao.save(group)
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

            group.apply {
                name = editCredentials.name
                description = editCredentials.description
                photoUrl = editCredentials.photoUrl
                private = editCredentials.isPrivate
                interests = interestsSet
            }

            groupDao.save(group)
        }

    fun getMeetings(groupId: Long): List<Meeting> =
        groupId.doIfExist(groupDao, logger) { group -> group.meetings }

    fun uploadImage(file: MultipartFile, groupId: Long): Group =
        groupId.doIfExist(groupDao, logger) { group ->
            val imageUrl = fileStoreService.store(file, group::class.java, group.id)
            group.photoUrl = imageUrl
            groupDao.save(group)
        }

    override fun getEntity(id: Long): Group? =
        id.getEntity(groupDao, logger)
}