package com.meetme.service.media_link

import com.meetme.db.user.User
import com.meetme.db.group.Group
import com.meetme.db.media_link.MediaLink
import com.meetme.db.media_link.MediaLinkDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Реализация сервиса для работы с ссылками на социальные сети.
 */
@Service
class MediaLinkServiceImpl @Autowired constructor(
    /**
     * Data access object предоставляющий доступ к таблице с ссылками на социальные сети.
     */
    private val mediaLinkDao: MediaLinkDao,
) : MediaLinkService {
    /**
     * Создает новые ссылки по пользователю и Map<String, String>
     */
    override fun createNewLinks(links: Map<String, String>, user: User): Set<MediaLink> {
        val linksSet = mutableSetOf<MediaLink>()
        for ((key, link) in links) {
            val newLink = mediaLinkDao.save(MediaLink(name = key, link = link))
            newLink.user = user
            linksSet.add(newLink)
        }

        return linksSet
    }
}