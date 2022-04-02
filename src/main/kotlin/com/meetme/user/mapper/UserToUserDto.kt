package com.meetme.user.mapper

import com.meetme.domain.dto.auth.UserDto
import com.meetme.interest.mapper.InterestsToStrings
import com.meetme.media_link.mapper.MediaLinksToMap
import com.meetme.user.db.User

interface UserToUserDto : (User) -> UserDto

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