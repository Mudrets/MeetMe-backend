package com.meetme.service.group

import com.meetme.util.doIfExist
import com.meetme.domain.dto.goup.CreateGroupDto
import com.meetme.domain.dto.goup.UpdateGroupDto
import com.meetme.db.meeting.Meeting
import com.meetme.db.group.Group
import com.meetme.db.group.GroupDao
import com.meetme.service.interest.InterestService
import com.meetme.service.user.UserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Реализация сервиса для работы с группами.
 */
@Service
class GroupServiceImpl @Autowired constructor(
    /**
     * Data access object для получения данных из таблицы групп в базе данных.
     */
    private val groupDao: GroupDao,
    /**
     * Серсив для работы с пользователем.
     */
    private val userService: UserService,
    /**
     * Сервис для работы с интересами.
     */
    private val interestService: InterestService,
) : GroupService {

    /**
     * Логгер для логгирования.
     */
    private val logger: Logger = LoggerFactory.getLogger(GroupServiceImpl::class.java)

    /**
     * Создает гурппу по переданным данным.
     * @param data данные для создания группы.
     * @return Возращает созданную группу.
     */
    override fun create(data: CreateGroupDto): Group =
        data.adminId.doIfExist(userService) { admin ->
            val interestsSet =
                interestService.convertToInterestEntityAndAddNewInterests(interests = data.interests)

            val group = Group(
                name = data.name,
                description = data.description,
                interests = interestsSet,
                admin = admin,
                isPrivate = data.isPrivate,
            )

            groupDao.save(group)
        }

    /**
     * Изменяет группу в соответствии с переданными данными.
     * @param identifier идентификатор группы.
     * @param data данные для изменения группы.
     * @return Возвращает измененную группу.
     */
    override fun update(identifier: Long, data: UpdateGroupDto): Group =
        identifier.doIfExist(groupDao, logger) { group ->
            val interestsSet =
                interestService.convertToInterestEntityAndAddNewInterests(interests = data.interests)

            group.apply {
                name = data.name
                description = data.description
                photoUrl = data.photoUrl
                isPrivate = data.isPrivate
                interests = interestsSet
            }

            groupDao.save(group)
        }

    /**
     * Получает список мероприятий в которых группа принимает участие.
     * @param groupId идентификатор группы.
     * @return Возвращает список мероприятий, в которых группа принимает участие.
     */
    override fun getMeetings(groupId: Long): List<Meeting> =
        groupId.doIfExist(groupDao, logger) { group -> group.meetings }

    /**
     * Сохроняет группу в хранилище.
     * @param entity сохраняемая группа.
     * @return Возвращает сохраненную группу.
     */
    override fun save(entity: Group): Group = groupDao.save(entity)

    /**
     * Удаляет переданную группу.
     * @param entity удаляемая группа.
     */
    override fun delete(entity: Group) = groupDao.delete(entity)

    /**
     * Удаляет группу по переданному идентификатору.
     * @param identifier идентификатор удаляемой группы.
     */
    override fun deleteByIdentifier(identifier: Long) = groupDao.deleteById(identifier)

    /**
     * Получает группу по переданному идентификатору.
     * @param identifier идентификатор.
     * @return Возвращает полученную группу.
     */
    override fun get(identifier: Long): Group =
        identifier.doIfExist(groupDao, logger) { it }

    /**
     * Получает все существующие группы.
     * @return Возвращает список всех групп.
     */
    override fun getAll(): List<Group> = groupDao.findAll()
}