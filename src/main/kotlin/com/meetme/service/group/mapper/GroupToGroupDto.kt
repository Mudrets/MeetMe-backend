package com.meetme.service.group.mapper

import com.meetme.domain.dto.goup.GroupDto
import com.meetme.db.group.Group
import com.meetme.service.interest.mapper.InterestsToStrings

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