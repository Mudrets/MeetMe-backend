package com.meetme.service.meeting

import com.meetme.service.chat.ChatService
import com.meetme.util.doIfExist
import com.meetme.domain.dto.meeting.CreateMeetingDto
import com.meetme.domain.dto.meeting.UpdateMeetingDto
import com.meetme.service.interest.InterestService
import com.meetme.db.meeting.Meeting
import com.meetme.db.meeting.MeetingDao
import com.meetme.service.user.UserServiceImpl
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Реализация сервиса для работы с мероприятиями.
 */
@Service
class MeetingServiceImpl @Autowired constructor(
    /**
     * Data access object предоставляющий доступ к таблицам мероприятий в базе данных.
     */
    private val meetingDao: MeetingDao,
    /**
     * Сервис для работы с пользователями.
     */
    private val userService: UserServiceImpl,
    /**
     * Сервис для работы с интересами.
     */
    private val interestService: InterestService,
    /**
     * Сервис для работы с чатом.
     */
    private val chatService: ChatService,
) : MeetingService {

    /**
     * Логгер для логгирования.
     */
    private val logger: Logger = LoggerFactory.getLogger(MeetingServiceImpl::class.java)

    /**
     * Создает мероприятие по переданным данным.
     * @param data данные для создания мероприятия.
     * @return Возращает созданную мероприятие.
     */
    override fun create(data: CreateMeetingDto): Meeting =
        data.adminId.doIfExist(userService) { admin ->
            val interestsSet =
                interestService.convertToInterestEntityAndAddNewInterests(interests = data.interests)

            val meeting = Meeting(
                name = data.name,
                description = data.description,
                startDate = data.startDate,
                endDate = data.endDate,
                isOnline = data.isOnline,
                isPrivate = data.isPrivate,
                interests = interestsSet,
                admin = admin,
                maxNumberOfParticipants = data.maxNumberOfParticipants,
                chat = chatService.createChat()
            )
            meetingDao.save(meeting)
        }

    /**
     * Изменяет мероприятие в соответствии с переданными данными.
     * @param identifier идентификатор мероприятия.
     * @param data данные для изменения мероприятия.
     * @return Возвращает измененное мероприятие.
     */
    override fun update(identifier: Long, data: UpdateMeetingDto): Meeting =
        identifier.doIfExist(meetingDao, logger) { meeting ->
            val interestsSet =
                interestService.convertToInterestEntityAndAddNewInterests(interests = data.interests)

            meeting.apply {
                name = data.name
                description = data.description
                startDate = data.startDate
                endDate = data.endDate
                isOnline = data.isOnline
                location = if (data.isOnline) data.locate else meeting.location
                maxNumberOfParticipants = data.maxNumberOfParticipants
                interests = interestsSet
            }
            meetingDao.save(meeting)
            meeting
        }

    /**
     * Сохроняет мероприятие в хранилище.
     * @param entity сохраняемое мероприятие.
     * @return Возвращает сохраненное мероприятие.
     */
    override fun save(entity: Meeting) = meetingDao.save(entity)

    /**
     * Удаляет мероприятие по переданному идентификатору.
     * @param identifier идентификатор удаляемого мероприятия.
     */
    override fun deleteByIdentifier(identifier: Long) = meetingDao.deleteById(identifier)

    /**
     * Удаляет переданное мероприятие.
     * @param entity удаляемое мероприятие.
     */
    override fun delete(entity: Meeting) = meetingDao.delete(entity)

    /**
     * Получает мероприятие по переданному идентификатору.
     * @param identifier идентификатор.
     * @return Возвращает полученное мероприятие.
     */
    override fun get(identifier: Long): Meeting =
        identifier.doIfExist(meetingDao, logger) { meeting -> meeting }

    /**
     * Получает все существующие мероприятия.
     * @return Возвращает список всех мероприятий.
     */
    override fun getAll(): List<Meeting> = meetingDao.findAll()
}