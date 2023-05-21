package ru.quipy.service

import org.springframework.stereotype.Service
import ru.quipy.api.*
import ru.quipy.core.EventSourcingService
import ru.quipy.logic.*
import java.util.*

@Service
class TaskService(
    val taskEsService: EventSourcingService<UUID, TaskAggregate, TaskAggregateState>,
    val projectionsService: ProjectionsService
) {

    fun createTask(taskName: String, projectId: UUID, creatorId: UUID): TaskCreatedEvent {
        val projectMembers = projectionsService.getProjectMembersByProjectId(projectId)
        if (projectMembers?.contains(creatorId) == true) {
            return taskEsService.create { it.create(UUID.randomUUID(), taskName, projectId) }
        }
        throw IllegalArgumentException("Forbidden call. The user with id=$creatorId isn't the project member.")
    }

    fun addExecutor(taskId: UUID, initiatorUserId: UUID, addUserId: UUID): TaskExecutorAddedEvent {
        val projectMembers = projectionsService.getProjectMembersByTaskId(taskId)
        if (projectMembers?.contains(initiatorUserId) == true && projectMembers.contains(addUserId)) {
            return taskEsService.update(taskId) { it.addExecutor(addUserId, initiatorUserId) }
        }
        throw IllegalArgumentException("Forbidden call. The users are out of the project members.")
    }

    fun changeTaskName(newTaskName: String, userId: UUID, taskId: UUID): TaskNameUpdatedEvent {
        if (projectionsService.getProjectMembersByTaskId(taskId)?.contains(userId) == true) {
            return taskEsService.update(taskId) { it.updateName(newTaskName, userId) }
        }
        throw IllegalArgumentException("Forbidden call. The user with id=$userId isn't the project member.")
    }

    fun changeStatus(taskId: UUID, statusName: String, statusColor: String, userId: UUID): TaskStatusChangedEvent {
        val newStatus = Status(statusName, statusColor)
        val statuses = projectionsService.getProjectStatusesByTaskId(taskId)
        if (
            !statuses.contains(newStatus)
            &&
            projectionsService.getProjectMembersByTaskId(taskId)?.contains(userId) == true
        ) {
            return taskEsService.update(taskId) { it.setStatus(newStatus, userId) }
        }
        throw IllegalArgumentException("Forbidden call. The User or Status is out of the rules")
    }
}
