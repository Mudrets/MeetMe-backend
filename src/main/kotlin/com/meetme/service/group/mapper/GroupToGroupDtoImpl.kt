package com.meetme.service.group.mapper

import com.meetme.db.group.Group
import com.meetme.domain.dto.goup.GroupDto
import com.meetme.service.interest.mapper.InterestsToStrings

/**
 * Маппер, преобразующий Group в GroupDto.
 */
class GroupToGroupDtoImpl(
    /**
     * Маппер, преобразующий List<Interest> в List<String>.
     */
    private val interestsToStrings: InterestsToStrings,
) : GroupToGroupDto {

    override fun invoke(group: Group): GroupDto =
        GroupDto(
            id = group.id,
            adminId = group.admin.id,
            name = group.name,
            description = group.description ?: "",
            photoUrl = group.photoUrl,
            isPrivate = group.isPrivate,
            interests = interestsToStrings(group.interests)
        )
}