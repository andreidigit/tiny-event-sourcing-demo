package ru.quipy.controller

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.quipy.api.*
import ru.quipy.core.EventSourcingService
import ru.quipy.logic.*
import java.util.*

@RestController
@RequestMapping("/projects")
class ProjectController(
    val projectEsService: EventSourcingService<UUID, ProjectAggregate, ProjectAggregateState>
) {

    @PostMapping("/{projectTitle}")
    fun createProject(@PathVariable projectTitle: String, @RequestParam creatorId: UUID): ProjectCreatedEvent {
        return projectEsService.create { it.create(UUID.randomUUID(), projectTitle, creatorId) }
    }

    @GetMapping("/{projectId}")
    fun getAccount(@PathVariable projectId: UUID): ProjectAggregateState? {
        return projectEsService.getState(projectId)
    }

    /*@PostMapping("/{projectId}/tasks/{taskName}")
    fun createTask(@PathVariable projectId: UUID, @PathVariable taskName: String): TaskCreatedEvent {
        return projectEsService.update(projectId) {
            it.addTask(taskName)
        }
    }*/

    @PostMapping("/{projectId}/members")
    fun addMember(
        @PathVariable projectId: UUID,
        @RequestParam initiatorUserId: UUID,
        @RequestParam addUserId: UUID
    ): MemberAddedEvent {
        return projectEsService.update(projectId) {
            it.addMember(initiatorUserId, addUserId)
        }
    }

    @PostMapping("/{projectId}/status")
    fun createStatus(
        @PathVariable projectId: UUID,
        @RequestParam statusName: String,
        @RequestParam statusColor: String
    ): StatusCreatedEvent {
        return projectEsService.update(projectId) {
            it.addStatus(statusName, statusColor)
        }
    }

    @DeleteMapping("/{projectId}/status")
    fun deleteStatus(
        @PathVariable projectId: UUID,
        @RequestParam statusName: String,
        @RequestParam statusColor: String
    ): StatusDeletedEvent {
        return projectEsService.update(projectId) {
            it.deleteStatus(statusName, statusColor)
        }
    }
}
