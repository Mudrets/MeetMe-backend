package com.meetme.meeting

import com.meetme.iterest.Interest
import com.meetme.iterest.InterestDao
import com.meetme.medialink.MediaLink
import com.meetme.medialink.MediaLinkDao
import com.meetme.auth.UserDao
import com.meetme.iterest.InterestService
import com.meetme.medialink.MediaLinkService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MeetingService {

    @Autowired
    private lateinit var meetingDao: MeetingDao

    @Autowired
    private lateinit var userDao: UserDao

    @Autowired
    private lateinit var interestService: InterestService

    @Autowired
    private lateinit var mediaLinkService: MediaLinkService

    fun createMeeting(
        adminId: Long,
        name: String,
        description: String = "",
        interests: Set<String> = setOf(),
        links: MutableMap<String, String> = mutableMapOf(),
    ): Meeting? {
        var meeting: Meeting? = null

        userDao.findById(adminId).ifPresent { admin ->
            val interestsSet =
                interestService.convertToInterestEntityAndAddNewInterests(interests = interests)

            val linksSet =
                mediaLinkService.createNewLinks(links = links)

            meeting = meetingDao.save(
                Meeting(
                    name = name,
                    description = description,
                    interests = interestsSet,
                    socialMediaLinks = linksSet,
                    admin = admin,
                )
            )
        }

        return meeting
    }
}