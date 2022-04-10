package com.meetme.meeting

import com.meetme.chat.ChatService
import com.meetme.doIfExist
import com.meetme.domain.AllEntitiesGetter
import com.meetme.domain.dto.meeting.CreateMeetingDto
import com.meetme.domain.dto.meeting.EditMeetingDto
import com.meetme.getEntity
import com.meetme.interest.InterestService
import com.meetme.meeting.db.Meeting
import com.meetme.meeting.db.MeetingDao
import com.meetme.user.UserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class MeetingServiceImpl : MeetingService, AllEntitiesGetter<Meeting> {

    private val logger: Logger = LoggerFactory.getLogger(MeetingServiceImpl::class.java)

    @Autowired
    private lateinit var meetingDao: MeetingDao

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var interestService: InterestService

    @Autowired
    private lateinit var chatService: ChatService

    fun createMeeting(createMeetingDto: CreateMeetingDto): Meeting =
        createMeetingDto.adminId.doIfExist(userService) { admin ->
            val interestsSet =
                interestService.convertToInterestEntityAndAddNewInterests(interests = createMeetingDto.interests)

            val meeting = Meeting(
                name = createMeetingDto.name,
                description = createMeetingDto.description,
                startDate = createMeetingDto.startDate,
                endDate = createMeetingDto.endDate,
                isOnline = createMeetingDto.isOnline,
                private = createMeetingDto.isPrivate,
                interests = interestsSet,
                admin = admin,
                maxNumberOfParticipants = createMeetingDto.maxNumberOfParticipants
            )
            meeting.chat = chatService.createChat(meeting)
            meetingDao.save(meeting)
        }

    fun getMeeting(meetingId: Long): Meeting =
        meetingId.doIfExist(meetingDao, logger) { meeting -> meeting }

    fun editMeeting(meetingId: Long, changes: EditMeetingDto): Meeting =
        meetingId.doIfExist(meetingDao, logger) { meeting ->
            val interestsSet =
                interestService.convertToInterestEntityAndAddNewInterests(interests = changes.interests)

            meeting.apply {
                name = changes.name
                description = changes.description
                startDate = changes.startDate
                endDate = changes.endDate
                isOnline = changes.isOnline
                location = if (changes.isOnline) changes.locate else meeting.location
                maxNumberOfParticipants = changes.maxNumberOfParticipants
                interests = interestsSet
            }
            meetingDao.save(meeting)
            meeting
        }

    fun deleteMeeting(meetingId: Long) =
        meetingId.doIfExist(meetingDao, logger) { meeting ->
            chatService.deleteChat(meeting.chat)
            meetingDao.delete(meeting)
        }

    override fun save(entity: Meeting) = meetingDao.save(entity)

    override fun getEntity(identifier: Long): Meeting? =
        identifier.getEntity(meetingDao, logger)

    override fun getAll(): List<Meeting> {
        TODO("Not yet implemented")
    }
}