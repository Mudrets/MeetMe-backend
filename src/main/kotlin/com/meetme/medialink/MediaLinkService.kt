package com.meetme.medialink

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MediaLinkService {

    @Autowired
    private lateinit var mediaLinkDao: MediaLinkDao

    fun createNewLinks(links: Map<String, String>): Set<MediaLink> {
        val linksSet = mutableSetOf<MediaLink>()
        for ((key, link) in links) {
            val newLink = mediaLinkDao.save(MediaLink(name = key, link = link))
            linksSet.add(newLink)
        }

        return linksSet
    }
}