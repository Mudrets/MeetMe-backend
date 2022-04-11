package com.meetme.db.media_link

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository("mediaLinkRepository")
interface MediaLinkDao: JpaRepository<MediaLink, Long>