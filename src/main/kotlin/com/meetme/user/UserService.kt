package com.meetme.user

import com.meetme.domain.CrudService
import com.meetme.domain.dto.auth.RegisterCredentialsDto
import com.meetme.domain.dto.user.EditUserDto
import com.meetme.user.db.User
import org.springframework.security.core.userdetails.UserDetailsService

interface UserService :
    CrudService<RegisterCredentialsDto, EditUserDto, Long, User>, UserDetailsService {

    fun loginUser(email: String, password: String): User
}