package com.meetme.meeting

import com.meetme.chat.ChatService
import com.meetme.user.db.User
import com.meetme.user.db.UserDao
import com.meetme.doIfExist
import com.meetme.domain.EntityGetter
import com.meetme.domain.dto.meeting.CreateMeetingDto
import com.meetme.domain.dto.meeting.EditMeetingDto
import com.meetme.domain.dto.meeting.MeetingDto
import com.meetme.domain.dto.meeting.SearchQuery
import com.meetme.domain.filter.InterestsFilter
import com.meetme.domain.filter.NameFilter
import com.meetme.domain.filter.FilterType
import com.meetme.getEntity
import com.meetme.invitation.db.Invitation
import com.meetme.file.FileStoreService
import com.meetme.group.db.Group
import com.meetme.interest.InterestService
import com.meetme.meeting.db.Meeting
import com.meetme.meeting.db.MeetingDao
import com.meetme.meeting.mapper.MeetingToMeetingDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class MeetingServiceImpl : MeetingService {

    private val logger: Logger = LoggerFactory.getLogger(MeetingServiceImpl::class.java)

    @Autowired
    private lateinit var meetingDao: MeetingDao

    @Autowired
    private lateinit var fileStoreService: FileStoreService

    @Autowired
    private lateinit var userDao: UserDao

    @Autowired
    private lateinit var interestService: InterestService

    @Autowired
    private lateinit var chatService: ChatService

    @Autowired
    private lateinit var meetingToMeetingDto: MeetingToMeetingDto

    @Autowired
    private lateinit var nameFilter: NameFilter

    @Autowired
    private lateinit var interestsFilter: InterestsFilter

    private fun Iterable<Meeting>.filter(searchQuery: SearchQuery) =
        this
            .asSequence()
            .filter { nameFilter(it, searchQuery.searchQuery) }
            .filter { interestsFilter(it, searchQuery.interests) }
            .sortedBy(Meeting::name)
            .toList()

    fun createMeeting(createMeetingDto: CreateMeetingDto): Meeting =
        createMeetingDto.adminId.doIfExist(userDao, logger) { admin ->
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

    fun searchPlanned(userId: Long, searchQuery: SearchQuery): Map<FilterType, List<Meeting>> =
        userId.doIfExist(userDao, logger) { user ->
            val resMap = mutableMapOf<FilterType, List<Meeting>>()
            val userMeetings = user
                .meetings
                .union(user.managedMeetings)
                .filter(Meeting::isPlannedMeeting)
                .toSet()

            resMap[FilterType.GLOBAL_FILTER] = meetingDao.findAllByPrivate(false)
                .filter(Meeting::isPlannedMeeting)
                .subtract(userMeetings)
                .filter(searchQuery)

            resMap[FilterType.MY_FILTER] = userMeetings
                .filter(searchQuery)

            resMap
        }

    fun searchVisited(userId: Long, searchQuery: SearchQuery): List<Meeting> =
        userId.doIfExist(userDao, logger) { user ->
            user
                .meetings
                .union(user.managedMeetings)
                .filter(Meeting::isVisitedMeeting)
                .filter(searchQuery)
        }

    private fun getInvitationsForGroup(group: Group, searchQuery: SearchQuery): List<MeetingDto> =
        group.invitations
            .map(Invitation::meeting)
            .filter(Meeting::isPlannedMeeting)
            .filter(searchQuery)
            .map { meeting -> meetingToMeetingDto(meeting, group.admin.id) }

    private fun getInvitationsForUser(user: User, searchQuery: SearchQuery): List<MeetingDto> =
        user.invitations
            .map(Invitation::meeting)
            .filter(Meeting::isPlannedMeeting)
            .filter(searchQuery)
            .map { meeting -> meetingToMeetingDto(meeting, user.id) }

    fun searchInvitations(userId: Long, searchQuery: SearchQuery): Map<String, List<MeetingDto>> =
        userId.doIfExist(userDao, logger) { user ->
            val nameWithIdToMeetings = mutableMapOf<String, List<MeetingDto>>()
            nameWithIdToMeetings["${user.fullName}:${user.id}"] =
                getInvitationsForUser(user, searchQuery)

            user.managedGroup
                .forEach { group ->
                    nameWithIdToMeetings["${group.name}:${group.id}"] =
                        getInvitationsForGroup(group, searchQuery)
                }
            nameWithIdToMeetings
        }

    private fun getAllMeetingsForUser(userId: Long): List<Meeting> =
        userId.doIfExist(userDao, logger) { user ->
            user.meetings.union(user.managedMeetings).toList()
        }

    fun getVisitedMeetingForUser(userId: Long): List<Meeting> =
        getAllMeetingsForUser(userId)
            .filter(Meeting::isVisitedMeeting)

    fun getPlannedMeetingsForUser(userId: Long): List<Meeting> =
        getAllMeetingsForUser(userId)
            .filter(Meeting::isPlannedMeeting)

    fun uploadImage(image: MultipartFile, meetingId: Long): Meeting =
        meetingId.doIfExist(meetingDao, logger) { meeting ->
            val imageUrl = fileStoreService.store(image, meeting::class.java, meeting.id)
            meeting.photoUrl = imageUrl
            meetingDao.save(meeting)
        }

    override fun save(entity: Meeting) = meetingDao.save(entity)

    override fun getEntity(id: Long): Meeting? =
        id.getEntity(meetingDao, logger)
}