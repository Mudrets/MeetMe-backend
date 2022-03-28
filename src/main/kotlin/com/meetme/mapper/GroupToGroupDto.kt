package com.meetme.mapper

import com.meetme.dto.goup.GroupDto
import com.meetme.group.Group

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
            isPrivate = group.isPrivate,
            posts = group.posts,
            interests = interestsToStrings(group.interests),
            socialMediaLinks = mediaLinksToMap(group.socialMediaLinks)
        )
}