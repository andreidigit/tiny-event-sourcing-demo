package ru.quipy.service

import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import ru.quipy.logic.Status
import java.util.*

@Service
class ProjectionsService(val webClient: WebClient) {

    //GET http://localhost:8088/projects/{{projectId}}/members
    fun getProjectMembersByProjectId(projectId: UUID): MutableList<UUID>? {
        return webClient
            .get()
            .uri("/projects/$projectId/members")
            .retrieve()
            .bodyToFlux(UUID::class.java)
            .collectList()
            .block()
    }

    //GET http://localhost:8088/tasks/{{taskId_1}}/project-members
    fun getProjectMembersByTaskId(taskId: UUID): MutableList<UUID>? {
        return webClient
            .get()
            .uri("/tasks/$taskId/project-members")
            .retrieve()
            .bodyToFlux(UUID::class.java)
            .collectList()
            .block()
    }

    //GET http://localhost:8088/tasks/{{taskId_1}}/statuses
    fun getProjectStatusesByTaskId(taskId: UUID): List<Status> {
        return webClient
            .get()
            .uri("/tasks/$taskId/statuses")
            .retrieve()
            .bodyToFlux(String::class.java)
            .collectList()
            .block()
            ?.map { Status(it.substringBefore(':'), it.substringAfter(':')) } ?: emptyList()
    }

    //GET http://localhost:8088/projects/{{projectId}}/statuses
    fun getProjectStatusesByProjectId(projectId: UUID): List<Status> {
        return webClient
            .get()
            .uri("/projects/$projectId/statuses")
            .retrieve()
            .bodyToFlux(Pair::class.java)
            .collectList()
            .block()
            ?.map { Status(it.first as String, it.second as String) } ?: emptyList()
    }
}
