package com.meetme.service.media_link

import com.meetme.db.media_link.MediaLink
import com.meetme.db.user.User

/**
 * Сервис для работы с ссылками на социальные сети.
 */
interface MediaLinkService {
    /**
     * Создает новые ссылки по пользователю и Map<String, String>
     */
    fun createNewLinks(links: Map<String, String>, user: User): Set<MediaLink>
}
