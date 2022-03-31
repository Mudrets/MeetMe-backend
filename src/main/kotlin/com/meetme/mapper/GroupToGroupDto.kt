package com.meetme.mapper

import com.meetme.domain.dto.goup.GroupDto
import com.meetme.services.group.Group

interface GroupToGroupDto : (Group) -> GroupDto

class GroupToGroupDtoImpl(
    private val interestsToStrings: InterestsToStrings,
    private val mediaLinksToMap: MediaLinksToMap,
) : GroupToGroupDto {

    override fun invoke(group: Group): GroupDto =
        GroupDto(
            id = group.id,
            adminId = group.admin.id,
            name = group.name,
            description = group.description ?: "",
            photoUrl = group.photoUrl,
            isPrivate = group.private,
            interests = interestsToStrings(group.interests),
            socialMediaLinks = mediaLinksToMap(group.socialMediaLinks)
        )
}