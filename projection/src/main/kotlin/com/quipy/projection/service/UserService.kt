package com.quipy.projection.service

import com.quipy.projection.entities.User
import com.quipy.projection.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(
    val userRepository: UserRepository
) {
    fun create(userId: String, userName: String): User {
        return userRepository.save(User(UUID.fromString(userId), userName))
    }

    fun getUserByUserId(userId: UUID): User? {
        return userRepository.getReferenceById(userId)
    }

}
