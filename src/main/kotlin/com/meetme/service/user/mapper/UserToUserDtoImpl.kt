package com.meetme.service.user.mapper

import com.meetme.db.user.User
import com.meetme.domain.dto.auth.UserDto
import com.meetme.service.interest.mapper.InterestsToStrings
import com.meetme.service.media_link.mapper.MediaLinksToMap

/**
 * Маппер, преобразующий User в UserDto.
 */
class UserToUserDtoImpl(
    private val interestsToStrings: InterestsToStrings,
    private val mediaLinksToMap: MediaLinksToMap,
) : UserToUserDto {

    override fun invoke(user: User): UserDto =
        UserDto(
            id = user.id,
            fullName = user.fullName,
            photoUrl = user.photoUrl,
            email = user.email,
            telephone = user.telephone,
            links = mediaLinksToMap(user.socialMediaLinks),
            interests = interestsToStrings(user.interests),
            description = user.description,
        )

}