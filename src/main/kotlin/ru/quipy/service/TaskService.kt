package ru.quipy.service

import org.springframework.stereotype.Service
import ru.quipy.api.*
import ru.quipy.core.EventSourcingService
import ru.quipy.logic.*
import java.util.*

@Service
class TaskService(
    val projectEsService: EventSourcingService<UUID, ProjectAggregate, ProjectAggregateState>,
    val taskEsService: EventSourcingService<UUID, TaskAggregate, TaskAggregateState>
) {

    fun createTask(taskName: String, projectId: UUID, creatorId: UUID): TaskCreatedEvent {
        if (projectEsService.getState(projectId)?.projectMembers?.contains(creatorId) == true) {
            return taskEsService.create { it.create(UUID.randomUUID(), taskName, projectId) }
        }
        throw IllegalArgumentException("Forbidden call. The user with id=$creatorId isn't the project member.")
    }

    fun addExecutor(taskId: UUID, initiatorUserId: UUID, addUserId: UUID): TaskExecutorAddedEvent {
        val projectMembers = this.getProjectState(taskId).projectMembers
        if (projectMembers.contains(initiatorUserId) && projectMembers.contains(addUserId)) {
            return taskEsService.update(taskId) { it.addExecutor(addUserId, initiatorUserId) }
        }
        throw IllegalArgumentException("Forbidden call. The users are out of the project members.")
    }

    fun changeTaskName(newTaskName: String, userId: UUID, taskId: UUID): TaskNameUpdatedEvent {
        if (this.getProjectState(taskId).projectMembers.contains(userId)) {
            return taskEsService.update(taskId) { it.updateName(newTaskName, userId) }
        }
        throw IllegalArgumentException("Forbidden call. The user with id=$userId isn't the project member.")
    }

    fun changeStatus(taskId: UUID, statusName: String, statusColor: String, userId: UUID): TaskStatusChangedEvent {
        val newStatus = Status(statusName, statusColor)
        val projectState = this.getProjectState(taskId)
        if (projectState.statuses.contains(newStatus) && projectState.projectMembers.contains(userId)) {
            return taskEsService.update(taskId){it.setStatus(newStatus, userId)}
        }
        throw IllegalArgumentException("Forbidden call. The User or Status is out of the rules")
    }

    private fun getProjectState(taskId: UUID): ProjectAggregateState {
        val projectId = taskEsService.getState(taskId)?.projectId
        var projectState: ProjectAggregateState? = null
        if (projectId != null) {
            projectState = projectEsService.getState(projectId)
        }
        return projectState
            ?: throw IllegalArgumentException("There is no Project associated to the Task with id=$taskId")
    }

    fun getTask(taskId: UUID): TaskAggregateState? {
        return taskEsService.getState((taskId))
    }

}
