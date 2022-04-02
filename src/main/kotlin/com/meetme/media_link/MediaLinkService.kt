package com.meetme.media_link

import com.meetme.user.db.User
import com.meetme.group.db.Group
import com.meetme.media_link.db.MediaLink
import com.meetme.media_link.db.MediaLinkDao
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