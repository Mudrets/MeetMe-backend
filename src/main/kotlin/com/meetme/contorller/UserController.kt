package com.meetme.contorller

import com.meetme.auth.dto.AuthorizationUserDto
import com.meetme.auth.dto.CredentialsDto
import com.meetme.auth.UserService
import com.meetme.data.DataResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/api/v1/user")
class UserController {

    @Autowired
    private val userService: UserService? = null

    @PostMapping("/register")
    fun register(@RequestBody credentials: CredentialsDto): ResponseEntity<DataResponse<AuthorizationUserDto>> {
        val newUser = userService?.createNewUserByEmailAndPass(email = credentials.email, password = credentials.password)
        return if (newUser != null)
            ResponseEntity.ok(DataResponse(data = AuthorizationUserDto(id = newUser.id)))
        else
            ResponseEntity.ok(DataResponse(message = "User already exist"))
    }

    @PostMapping("/login")
    fun login(@RequestBody credentials: CredentialsDto): ResponseEntity<DataResponse<AuthorizationUserDto>> {
        val dbUser = userService?.loadUserByEmail(credentials.email)
            ?: return ResponseEntity.ok(DataResponse(message = "User does not exist"))

        return if (userService.checkPassword(user = dbUser, password = credentials.password))
            ResponseEntity.ok(
                DataResponse(
                    data = AuthorizationUserDto(id = dbUser.id)
                )
            )
        else
            ResponseEntity.ok(DataResponse(message = "Incorrect password"))
    }

}