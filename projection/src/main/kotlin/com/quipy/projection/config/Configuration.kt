package com.quipy.projection.config

import com.quipy.projection.entities.User
import org.springframework.boot.ApplicationRunner
import com.quipy.projection.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
class Configuration {

    private val logger = LoggerFactory.getLogger(Configuration::class.java)

    @Bean
    fun databaseInitializer(userRepository: UserRepository) =
        ApplicationRunner {

            userRepository.save(User(UUID.fromString("76177f67-6805-40ac-b6ea-522f8f796280"), "John"))
            logger.info("initialised")
        }
}
