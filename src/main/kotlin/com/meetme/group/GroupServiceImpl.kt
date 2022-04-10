package com.meetme.group

import com.meetme.user.db.UserDao
import com.meetme.doIfExist
import com.meetme.domain.dto.goup.CreateGroupDto
import com.meetme.domain.dto.goup.UpdateGroupDto
import com.meetme.meeting.db.Meeting
import com.meetme.group.db.Group
import com.meetme.group.db.GroupDao
import com.meetme.interest.InterestService
import com.meetme.user.UserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GroupServiceImpl : GroupService {

    private val logger: Logger = LoggerFactory.getLogger(GroupServiceImpl::class.java)

    @Autowired
    private lateinit var groupDao: GroupDao

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var interestService: InterestService

    override fun create(data: CreateGroupDto): Group =
        data.adminId.doIfExist(userService) { admin ->
            val interestsSet =
                interestService.convertToInterestEntityAndAddNewInterests(interests = data.interests)

            val group = Group(
                name = data.name,
                description = data.description,
                interests = interestsSet,
                admin = admin,
                private = data.isPrivate,
            )

            groupDao.save(group)
        }

    fun getGroup(groupId: Long): Group =
        groupId.doIfExist(groupDao, logger) { group -> group }

    override fun update(identifier: Long, data: UpdateGroupDto): Group =
        identifier.doIfExist(groupDao, logger) { group ->
            val interestsSet =
                interestService.convertToInterestEntityAndAddNewInterests(interests = data.interests)

            group.apply {
                name = data.name
                description = data.description
                photoUrl = data.photoUrl
                private = data.isPrivate
                interests = interestsSet
            }

            groupDao.save(group)
        }

    override fun getMeetings(groupId: Long): List<Meeting> =
        groupId.doIfExist(groupDao, logger) { group -> group.meetings }

    override fun save(entity: Group): Group = groupDao.save(entity)

    override fun delete(entity: Group) = groupDao.delete(entity)

    override fun deleteByIdentifier(identifier: Long) = groupDao.deleteById(identifier)

    override fun get(identifier: Long): Group =
        identifier.doIfExist(groupDao, logger) { it }

    override fun getAll(): List<Group> = groupDao.findAll()
}