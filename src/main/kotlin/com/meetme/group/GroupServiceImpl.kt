package com.meetme.group

import com.meetme.user.db.UserDao
import com.meetme.doIfExist
import com.meetme.domain.dto.goup.CreateGroupDto
import com.meetme.domain.dto.goup.EditGroupDto
import com.meetme.meeting.db.Meeting
import com.meetme.getEntity
import com.meetme.group.db.Group
import com.meetme.group.db.GroupDao
import com.meetme.interest.InterestService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class GroupServiceImpl : GroupService {

    private val logger: Logger = LoggerFactory.getLogger(GroupServiceImpl::class.java)

    @Autowired
    private lateinit var groupDao: GroupDao

    @Autowired
    private lateinit var userDao: UserDao

    @Autowired
    private lateinit var interestService: InterestService

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

    override fun save(entity: Group): Group = groupDao.save(entity)

    override fun getEntity(identifier: Long): Group? =
        identifier.getEntity(groupDao, logger)

    override fun getAll(): List<Group> {
        TODO("Not yet implemented")
    }
}