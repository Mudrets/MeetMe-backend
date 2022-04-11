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

@Service
class MeetingServiceImpl : MeetingService {

    private val logger: Logger = LoggerFactory.getLogger(MeetingServiceImpl::class.java)

    @Autowired
    private lateinit var meetingDao: MeetingDao

    @Autowired
    private lateinit var userService: UserServiceImpl

    @Autowired
    private lateinit var interestService: InterestService

    @Autowired
    private lateinit var chatService: ChatService

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
                private = data.isPrivate,
                interests = interestsSet,
                admin = admin,
                maxNumberOfParticipants = data.maxNumberOfParticipants,
                chat = chatService.createChat()
            )
            meetingDao.save(meeting)
        }

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

    override fun save(entity: Meeting) = meetingDao.save(entity)

    override fun deleteByIdentifier(identifier: Long) = meetingDao.deleteById(identifier)

    override fun delete(entity: Meeting) = meetingDao.delete(entity)

    override fun get(identifier: Long): Meeting =
        identifier.doIfExist(meetingDao, logger) { meeting -> meeting }

    override fun getAll(): List<Meeting> = meetingDao.findAll()
}