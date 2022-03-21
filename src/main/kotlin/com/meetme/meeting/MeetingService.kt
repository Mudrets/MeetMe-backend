package com.meetme.meeting

import com.meetme.auth.User
import com.meetme.auth.UserDao
import com.meetme.doIfExist
import com.meetme.dto.meeting.CreateMeetingDto
import com.meetme.dto.meeting.EditMeetingDto
import com.meetme.group.Group
import com.meetme.iterest.InterestService
import com.meetme.medialink.MediaLinkService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class MeetingService {

    private val logger: Logger = LoggerFactory.getLogger(MeetingService::class.java)

    @Autowired
    private lateinit var meetingDao: MeetingDao

    @Autowired
    private lateinit var userDao: UserDao

    @Autowired
    private lateinit var interestService: InterestService

    @Autowired
    private lateinit var mediaLinkService: MediaLinkService

    fun createMeeting(createMeetingDto: CreateMeetingDto): Meeting? {
        var meeting: Meeting? = null

        userDao.findById(createMeetingDto.adminId).ifPresent { admin ->
            val interestsSet =
                interestService.convertToInterestEntityAndAddNewInterests(interests = createMeetingDto.interests)

            val linksSet =
                mediaLinkService.createNewLinks(links = createMeetingDto.links)

            meeting = meetingDao.save(
                Meeting(
                    name = createMeetingDto.name,
                    description = createMeetingDto.description,
                    startDate = Date(createMeetingDto.startDate),
                    endDate = if (createMeetingDto.endDate == null) null else Date(createMeetingDto.endDate),
                    isOnline = createMeetingDto.isOnline,
                    private = createMeetingDto.isPrivate,
                    interests = interestsSet,
                    socialMediaLinks = linksSet,
                    admin = admin,
                )
            )
        }

        return meeting
    }

    fun getMeeting(meetingId: Long): Meeting =
        meetingId.doIfExist(meetingDao, logger) { meeting -> meeting }

    fun editMeeting(meetingId: Long, changes: EditMeetingDto): Meeting =
        meetingId.doIfExist(meetingDao, logger) { meeting ->
            meeting.apply {
                name = changes.name ?: meeting.name
                description = changes.description ?: meeting.description
                startDate = changes.startDate ?: meeting.startDate
                endDate = if (changes.hasEndDate) changes.endDate else meeting.endDate
                isOnline = changes.isOnline
                location = if (changes.isOnline) meeting.location else changes.locate
            }
            meetingDao.save(meeting)
            meeting
        }

    fun deleteMeeting(meetingId: Long) =
        meetingId.doIfExist(meetingDao, logger) { meeting -> meetingDao.delete(meeting) }

    fun addParticipant(meetingId: Long, userId: Long): Meeting =
        (meetingId to userId).doIfExist(meetingDao, userDao, logger) { meeting, user ->
            if (meeting.numberOfParticipants >= meeting.maxNumberOfParticipants)
                throw IllegalArgumentException("The maximum number of participants in the meeting has been exceeded")

            user.meetings.add(meeting)
            meeting.participants.add(user)
            meetingDao.save(meeting)
            userDao.save(user)
            meeting
        }

    fun deleteParticipant(meetingId: Long, userId: Long): Meeting =
        (meetingId to userId).doIfExist(meetingDao, userDao, logger) { meeting, user ->
            if (meeting.admin == user) {
                user.managedMeetings.remove(meeting)
                meetingDao.delete(meeting)
                userDao.save(user)
                meeting
            } else {
                meeting.participants.remove(user)
                user.meetings.remove(meeting)
                meetingDao.save(meeting)
                userDao.save(user)
                meeting
            }
        }

    fun search(userId: Long, searchQuery: String): List<Meeting> =
        meetingDao.findAllByPrivate(false)
            .filter { meeting -> meeting.name.contains(searchQuery) }
            .union(getAllMeetingsForUser(userId))
            .toList()

    fun getAllMeetingsForUser(userId: Long): List<Meeting> =
        userId.doIfExist(userDao, logger) { user ->
            user.meetings.union(user.managedMeetings).toList()
        }

    fun getParticipants(meetingId: Long): List<User> =
        meetingId.doIfExist(meetingDao, logger, Meeting::participants)
}