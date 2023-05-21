package com.quipy.projection.controller

import com.quipy.projection.entities.Project
import com.quipy.projection.entities.Status
import com.quipy.projection.service.ProjectService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/projects")
class ProjectController(
    val projectService: ProjectService
) {

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun createProject(
        @RequestParam projectTitle: String,
        @RequestParam projectId: UUID,
        @RequestParam creatorId: UUID,
        @RequestParam statusName: String,
        @RequestParam statusColor: String,
    ): Project {
        return projectService.create(projectTitle, projectId, creatorId, statusName, statusColor)
    }

    @PostMapping("/{projectId}/status")
    @ResponseStatus(HttpStatus.CREATED)
    fun addStatus(
        @PathVariable projectId: UUID,
        @RequestParam statusName: String,
        @RequestParam statusColor: String
    ): Status {
        return projectService.addStatus(projectId, statusName, statusColor)
    }

    @DeleteMapping("/{projectId}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteStatus(
        @PathVariable projectId: UUID,
        @RequestParam statusName: String,
        @RequestParam statusColor: String
    ) {
        projectService.deleteStatus(projectId, statusName, statusColor)
    }

    @PostMapping("/{projectId}/members")
    @ResponseStatus(HttpStatus.OK)
    fun addUser(
        @PathVariable projectId: UUID,
        @RequestParam userId: UUID
    ) {
        return projectService.addMember(projectId, userId)
    }

    @GetMapping("/{projectId}/statuses")
    fun getStatuses(
        @PathVariable("projectId") projectId: UUID
    ): List<Pair<String, String>> {
        return projectService.getStatuses(projectId)
    }

    @GetMapping("{projectId}")
    fun getProject(
        @PathVariable projectId: UUID
    ): Project {
        return projectService.getById(projectId)
    }

    @GetMapping("/{projectId}/members")
    fun getMembers(
        @PathVariable("projectId") projectId: UUID
    ): List<UUID>{
        return projectService.getMembers(projectId)
    }

}
