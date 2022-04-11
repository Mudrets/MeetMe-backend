package com.meetme.service.media_link

import com.meetme.db.user.User
import com.meetme.db.group.Group
import com.meetme.db.media_link.MediaLink
import com.meetme.db.media_link.MediaLinkDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MediaLinkService {

    @Autowired
    private lateinit var mediaLinkDao: MediaLinkDao

    fun createNewLinks(links: Map<String, String>, group: Group): Set<MediaLink> {
        val linksSet = mutableSetOf<MediaLink>()
        for ((key, link) in links) {
            val newLink = mediaLinkDao.save(MediaLink(name = key, link = link))
            newLink.group = group
            linksSet.add(newLink)
        }

        return linksSet
    }

    fun createNewLinks(links: Map<String, String>, user: User): Set<MediaLink> {
        val linksSet = mutableSetOf<MediaLink>()
        for ((key, link) in links) {
            val newLink = mediaLinkDao.save(MediaLink(name = key, link = link))
            newLink.user = user
            linksSet.add(newLink)
        }

        return linksSet
    }
}