package com.meetme.service.media_link.mapper

import com.meetme.db.media_link.MediaLink

/**
 * Маппер, представляющий Set<MediaLink> в виде Map<String, String>.
 */
interface MediaLinksToMap : (Set<MediaLink>) -> Map<String, String>

