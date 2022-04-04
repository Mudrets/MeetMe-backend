package com.meetme.domain.filter

import com.meetme.domain.dto.auth.UserDto

interface UserSearchFilter : (UserDto) -> Boolean {

    var searchString: String
}

class UserSearchFilterImpl : UserSearchFilter {

    override var searchString: String = ""

    override fun invoke(user: UserDto): Boolean =
        user.fullName.contains(searchString, false)

}