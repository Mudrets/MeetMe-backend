package com.meetme.mapper

import com.meetme.auth.User
import com.meetme.data.dto.auth.UserDto

interface UserToUserDto : (User) -> UserDto

class UserToUserDtoImpl(
    private val interestsToStrings: InterestsToStrings,
    private val mediaLinksToMap: MediaLinksToMap,
) : UserToUserDto {

    override fun invoke(user: User): UserDto =
        UserDto(
            id = user.id,
            fullName = user.fullName,
            photoUrl = if (user.photoUrl == null) null else "http://localhost:8080/${user.photoUrl}",
            email = user.email,
            telephone = user.telephone,
            links = mediaLinksToMap(user.socialMediaLinks),
            interests = interestsToStrings(user.interests),
            description = user.description,
        )

}