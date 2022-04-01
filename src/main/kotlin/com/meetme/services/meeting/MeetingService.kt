package com.meetme.services.meeting

import com.meetme.services.auth.User
import com.meetme.services.auth.UserDao
import com.meetme.doIfExist
import com.meetme.domain.dto.meeting.CreateMeetingDto
import com.meetme.domain.dto.meeting.EditMeetingDto
import com.meetme.domain.dto.meeting.SearchQuery
import com.meetme.domain.filter.InterestsFilter
import com.meetme.domain.filter.NameFilter
import com.meetme.domain.filter.FilterType
import com.meetme.services.chat.ChatService
import com.meetme.services.file.FileStoreService
import com.meetme.services.invitation.group.InvitationGroupToMeetingService
import com.meetme.services.invitation.participant.Invitation
import com.meetme.services.invitation.participant.InvitationService
import com.meetme.services.iterest.InterestService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

@Service
class MeetingService {

    private val logger: Logger = LoggerFactory.getLogger(MeetingService::class.java)

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
    private lateinit var invitationService: InvitationService

    @Autowired
    private lateinit var invitationGroupToMeetingService: InvitationGroupToMeetingService

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
                maxNumberOfParticipants = createMeetingDto.maxNumberParticipants
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
            invitationService.removeAllMeetingInvitations(meeting)
            invitationGroupToMeetingService.removeAllMeetingInvitation(meeting)
            meetingDao.delete(meeting)
        }

    fun addParticipant(meetingId: Long, userId: Long): Meeting =
        (meetingId to userId).doIfExist(meetingDao, userDao, logger) { meeting, user ->
            if (meeting.numberOfParticipants >= meeting.maxNumberOfParticipants)
                throw IllegalArgumentException("The maximum number of participants in the meeting has been exceeded")
            if (meeting.participants.contains(user))
                throw IllegalArgumentException(
                    "User with id = ${user.id} already is participant of meeting with id = ${meeting.id}"
                )

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
                invitationService.removeAllMeetingInvitations(meeting)
                invitationGroupToMeetingService.removeAllMeetingInvitation(meeting)
                meetingDao.delete(meeting)
                userDao.save(user)
                meeting
            } else {
                if (!meeting.participants.contains(user))
                    throw IllegalArgumentException(
                        "The user with id = $userId is not a member of the meeting $meetingId"
                    )

                meeting.participants.remove(user)
                user.meetings.remove(meeting)
                meetingDao.save(meeting)
                userDao.save(user)
                meeting
            }
        }

    fun searchPlanned(userId: Long, searchQuery: SearchQuery): Map<FilterType, List<Meeting>> =
        userId.doIfExist(userDao, logger) { user ->
            val resMap = mutableMapOf<FilterType, List<Meeting>>()
            val userMeetings = user
                .meetings
                .union(user.managedMeetings)
                .filter { meeting -> !isVisitedMeeting(meeting) }
                .toSet()

            resMap[FilterType.GLOBAL_FILTER] = meetingDao.findAllByPrivate(false)
                .filter { meeting -> !isVisitedMeeting(meeting) }
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
                .filter(::isVisitedMeeting)
                .filter(searchQuery)
        }

    fun searchInvites(userId: Long, searchQuery: SearchQuery): Map<String, List<Meeting>> =
        userId.doIfExist(userDao, logger) { user ->
            val resMap = mutableMapOf<String, List<Meeting>>()
            val personalInvitesMeetings = invitationService.getAllInvitationsForUser(user)
                .mapNotNull { invitation -> invitation.meeting }
                .filter { meeting -> !isVisitedMeeting(meeting) }
                .filter(searchQuery)
            resMap["${user.fullName}:${user.id}"] = personalInvitesMeetings

            user.managedGroup
                .map { group ->
                    val meetings = invitationGroupToMeetingService.getAllInvitationForGroup(group)
                        .mapNotNull { invitation -> invitation.meeting }
                        .filter { meeting -> !isVisitedMeeting(meeting) }
                        .filter(searchQuery)
                    group to meetings
                }
                .forEach { (group, meetings) ->
                    resMap["${group.name}:${group.id}"] = meetings
                }

            resMap
        }

    private fun getAllMeetingsForUser(userId: Long): List<Meeting> =
        userId.doIfExist(userDao, logger) { user ->
            user.meetings.union(user.managedMeetings).toList()
        }

    fun getVisitedMeetingForUser(userId: Long): List<Meeting> =
        getAllMeetingsForUser(userId)
            .filter { meeting -> isVisitedMeeting(meeting) }

    fun getPlannedMeetingsForUser(userId: Long): List<Meeting> =
        getAllMeetingsForUser(userId)
            .filter { meeting -> !isVisitedMeeting(meeting) }

    fun getInvitesOnMeetings(userId: Long): Map<String, List<Meeting>> =
        userId.doIfExist(userDao, logger) { user ->
            val resMap = mutableMapOf<String, List<Meeting>>()
            val personalInvitesMeetings = invitationService.getAllInvitationsForUser(user)
                .mapNotNull { invitation -> invitation.meeting }
                .filter { meeting -> !isVisitedMeeting(meeting) }
            resMap["${user.fullName}:${user.id}"] = personalInvitesMeetings

            user.managedGroup
                .map { group ->
                    val meetings = invitationGroupToMeetingService.getAllInvitationForGroup(group)
                        .mapNotNull { invitation -> invitation.meeting }
                        .filter { meeting -> !isVisitedMeeting(meeting) }
                    group to meetings
                }
                .forEach { (group, meetings) ->
                    resMap["${group.name}:${group.id}"] = meetings
                }

            resMap
        }

    fun getParticipants(meetingId: Long): List<User> =
        meetingId.doIfExist(meetingDao, logger, Meeting::participants)

    fun sendInvitation(userId: Long, meetingId: Long): Invitation =
        (userId to meetingId).doIfExist(userDao, meetingDao, logger) { user, meeting ->
            invitationService.sendInvitation(user, meeting)
        }

    fun acceptInvitation(userId: Long, meetingId: Long): Invitation =
        (userId to meetingId).doIfExist(userDao, meetingDao, logger) { user, meeting ->
            val invitation = invitationService.acceptInvitation(user, meeting)
            meeting.participants.add(user)
            user.meetings.add(meeting)
            meetingDao.save(meeting)
            userDao.save(user)
            invitation
        }

    fun cancelInvitation(userId: Long, meetingId: Long): Invitation =
        (userId to meetingId).doIfExist(userDao, meetingDao, logger) { user, meeting ->
            val invitation = invitationService.cancelInvitation(user, meeting)
            meeting.participants.remove(user)
            user.meetings.remove(meeting)
            meetingDao.save(meeting)
            userDao.save(user)
            invitation
        }

    private fun isVisitedMeeting(meeting: Meeting): Boolean {
        val now = Date.from(Instant.now())
        val format = SimpleDateFormat("MM-dd-yyyy HH:mm")
        val endDateStr = meeting.endDate
        return endDateStr != null && format.parse(endDateStr).before(now)
    }

    fun sendInvitationToUsers(userIds: List<Long>, meetingId: Long) {
        (meetingId to userIds).doIfExist(meetingDao, userDao, logger) { meeting, user ->
            invitationService.sendInvitation(
                user = user,
                meeting = meeting,
            )
        }
    }

    fun uploadImage(image: MultipartFile, meetingId: Long): Meeting =
        meetingId.doIfExist(meetingDao, logger) { meeting ->
            val imageUrl = fileStoreService.store(image, meeting::class.java, meeting.id)
            meeting.photoUrl = imageUrl
            meetingDao.save(meeting)
        }
}