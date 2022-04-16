package com.meetme.db.media_link

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Интерфейс предоставляющий абстракцию для работы с таблицей ссылок в базе данных.
 */
@Repository("mediaLinkRepository")
interface MediaLinkDao: JpaRepository<MediaLink, Long>