package com.meetme.contorller

import com.meetme.auth.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("api/v1/users")
class UserController {

    @Autowired
    private val userService: UserService? = null

}