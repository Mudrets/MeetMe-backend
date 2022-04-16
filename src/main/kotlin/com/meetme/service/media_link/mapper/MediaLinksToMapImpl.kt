package com.meetme.service.media_link.mapper

import com.meetme.db.media_link.MediaLink

/**
 * Маппер, представляющий Set<MediaLink> в виде Map<String, String>,
 * где ключ - это навзание ссылки, а значение - это сама ссылка.
 */
class MediaLinksToMapImpl : MediaLinksToMap {
    override fun invoke(mediaLinks: Set<MediaLink>): Map<String, String> {
        val nameToLinkPairs =
            mediaLinks
                .map { link -> (link.name ?: "") to link.link }
        return mapOf(*nameToLinkPairs.toTypedArray())
    }
}