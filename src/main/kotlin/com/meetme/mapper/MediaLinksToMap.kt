package com.meetme.mapper

import com.meetme.medialink.MediaLink

interface MediaLinksToMap : (Set<MediaLink>) -> Map<String, String>

class MediaLinksToMapImpl : MediaLinksToMap {
    override fun invoke(mediaLinks: Set<MediaLink>): Map<String, String> {
        val nameToLinkPairs =
            mediaLinks
                .map { link -> (link.name ?: "") to link.link }
        return mapOf(*nameToLinkPairs.toTypedArray())
    }
}
