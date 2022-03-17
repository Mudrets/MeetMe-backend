package com.meetme.group

import com.meetme.auth.UserDao
import com.meetme.iterest.InterestService
import com.meetme.medialink.MediaLinkService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GroupService {

    @Autowired
    private lateinit var groupDao: GroupDao

    @Autowired
    private lateinit var userDao: UserDao

    @Autowired
    private lateinit var interestService: InterestService

    @Autowired
    private lateinit var mediaLinkService: MediaLinkService

    fun createGroup(
        adminId: Long,
        name: String,
        description: String = "",
        interests: Set<String> = setOf(),
        links: MutableMap<String, String> = mutableMapOf(),
    ): Group? {
        var meeting: Group? = null

        userDao.findById(adminId).ifPresent { admin ->
            val interestsSet =
                interestService.convertToInterestEntityAndAddNewInterests(interests = interests)

            val linksSet =
                mediaLinkService.createNewLinks(links = links)

            meeting = groupDao.save(
                Group(
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