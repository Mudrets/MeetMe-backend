package com.meetme.contorller

import com.meetme.auth.CredentialsDto
import com.meetme.auth.User
import com.meetme.auth.UserService
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

    @PostMapping
    fun register(@RequestBody credentials: CredentialsDto): ResponseEntity<User> {
        val newUser = userService?.createNewUser(email = credentials.email, password = credentials.password)
        return if (newUser != null)
            ResponseEntity.ok(newUser)
        else
            ResponseEntity.badRequest().build()
    }

}