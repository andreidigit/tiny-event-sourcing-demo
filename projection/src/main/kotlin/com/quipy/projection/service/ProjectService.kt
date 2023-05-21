package com.quipy.projection.service

import com.quipy.projection.entities.*
import com.quipy.projection.repository.ProjectRepository
import com.quipy.projection.repository.ProjectsUsersRepository
import com.quipy.projection.repository.StatusRepository
import com.quipy.projection.repository.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class ProjectService(
    val projectRepository: ProjectRepository,
    val userRepository: UserRepository,
    val projectsUsersRepository: ProjectsUsersRepository,
    val statusRepository: StatusRepository
) {

    val logger: Logger = LoggerFactory.getLogger(ProjectService::class.java)

    @Transactional
    fun create(
        projectTitle: String,
        projectId: UUID,
        creatorId: UUID,
        statusName: String,
        statusColor: String
    ): Project {
        projectsUsersRepository.save(ProjectsUsers(projectId, creatorId))
        statusRepository.save(Status(null, projectId, statusName, statusColor))
        return projectRepository.save(Project(projectId, projectTitle, creatorId))
    }

    fun addStatus(projectId: UUID, statusName: String, statusColor: String): Status {
        return statusRepository.save(Status(null, projectId, statusName, statusColor))
    }

    fun deleteStatus(projectId: UUID, statusName: String, statusColor: String) {
        statusRepository
            .findByProjectId(projectId)
            .firstOrNull {it.statusName == statusName && it.statusColor == statusColor}
            ?.let { statusRepository.delete(it) }
    }

    fun addMember(projectId: UUID, userId: UUID) {
        projectsUsersRepository.save(ProjectsUsers(projectId, userId))
    }

    fun getById(projectId: UUID): Project {
        return projectRepository.getReferenceById(projectId)
    }

    fun getStatuses(projectId: UUID): List<Pair<String, String>> {
        return statusRepository.findByProjectId(projectId).map{Pair(it.statusName, it.statusColor)}
    }

    fun getMembers(projectId: UUID): List<UUID> {
        return projectsUsersRepository.findByProjectId(projectId).map { it.userId }
    }

}
