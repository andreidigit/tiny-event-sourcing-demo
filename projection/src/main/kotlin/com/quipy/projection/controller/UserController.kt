package com.quipy.projection.controller

import com.quipy.projection.entities.User
import com.quipy.projection.service.UserService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/users")
class UserController(
    val userService: UserService
) {

    @PostMapping(path = [""], consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun createUser(
        @RequestParam(name = "userId") userId: String,
        @RequestParam(name = "userName") userName: String
    ): User {
        return userService.create(userId, userName)
    }

    @GetMapping(path = ["/{userId}"])
    fun getUser(@PathVariable("userId") userId: UUID): User? {
        return userService.getUserByUserId(userId)
    }
}
