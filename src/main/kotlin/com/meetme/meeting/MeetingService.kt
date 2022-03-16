package com.meetme.meeting

import com.meetme.auth.UserDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MeetingService {

    @Autowired
    private lateinit var meetingDao: MeetingDao

    @Autowired
    private lateinit var userDao: UserDao

    @Autowired
    private lateinit var interestDao: InterestDao

    @Autowired
    private lateinit var mediaLinkDao: MediaLinkDao

    fun createMeeting(
        adminId: Long,
        name: String,
        description: String = "",
        interests: Set<String> = setOf(),
        links: MutableMap<String, String> = mutableMapOf(),
    ): Meeting? {
        var meeting: Meeting? = null
        userDao.findById(adminId).ifPresent { admin ->
            val interestsSet = mutableSetOf<Interest>()
            for (interestName in interests) {
                val dbInterest = interestDao.findByName(interestName) ?: interestDao.save(Interest(name = interestName))
                interestsSet.add(dbInterest)
            }

            val linksSet = mutableSetOf<MediaLink>()
            for ((key, link) in links) {
                val newLink = mediaLinkDao.save(MediaLink(name = key, link = link))
                linksSet.add(newLink)
            }

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