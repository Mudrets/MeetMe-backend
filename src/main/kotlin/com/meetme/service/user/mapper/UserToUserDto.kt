package com.meetme.service.user.mapper

import com.meetme.db.user.User
import com.meetme.domain.dto.auth.UserDto

/**
 * Маппер, преобразующий User в UserDto.
 */
interface UserToUserDto : (User) -> UserDto