package com.meetme.group.mapper

import com.meetme.domain.dto.goup.GroupDto
import com.meetme.group.db.Group
import com.meetme.interest.mapper.InterestsToStrings
import com.meetme.media_link.mapper.MediaLinksToMap

interface GroupToGroupDto : (Group) -> GroupDto

class GroupToGroupDtoImpl(
    private val interestsToStrings: InterestsToStrings,
) : GroupToGroupDto {

    override fun invoke(group: Group): GroupDto =
        GroupDto(
            id = group.id,
            adminId = group.admin.id,
            name = group.name,
            description = group.description ?: "",
            photoUrl = group.photoUrl,
            isPrivate = group.private,
            interests = interestsToStrings(group.interests)
        )
}