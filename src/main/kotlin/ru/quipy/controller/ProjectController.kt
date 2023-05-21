package ru.quipy.controller

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.quipy.api.*
import ru.quipy.logic.*
import ru.quipy.service.ProjectService
import java.util.*

@RestController
@RequestMapping("/projects")
class ProjectController(
    val projectService: ProjectService
) {

    @PostMapping("/{projectTitle}")
    fun createProject(@PathVariable projectTitle: String, @RequestParam creatorId: UUID): ProjectCreatedEvent {
        return projectService.create(UUID.randomUUID(), projectTitle, creatorId)
    }

    @PostMapping("/{projectId}/members")
    fun addMember(
        @PathVariable projectId: UUID,
        @RequestParam initiatorUserId: UUID,
        @RequestParam addUserId: UUID
    ): MemberAddedEvent {
        return projectService.addMember(projectId, initiatorUserId, addUserId)
    }

    @PostMapping("/{projectId}/status")
    fun createStatus(
        @PathVariable projectId: UUID,
        @RequestParam statusName: String,
        @RequestParam statusColor: String
    ): StatusCreatedEvent {
        return projectService.addStatus(projectId, statusName, statusColor)
    }

    @DeleteMapping("/{projectId}/status")
    fun deleteStatus(
        @PathVariable projectId: UUID,
        @RequestParam statusName: String,
        @RequestParam statusColor: String
    ): StatusDeletedEvent {
        return projectService.deleteStatus(projectId, statusName, statusColor)
    }
}
