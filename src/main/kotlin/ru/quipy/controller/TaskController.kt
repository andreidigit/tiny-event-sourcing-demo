package ru.quipy.controller

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.quipy.api.*
import ru.quipy.logic.*
import ru.quipy.service.TaskService
import java.util.*

@RestController
@RequestMapping("/tasks")
class TaskController(
    val taskService: TaskService
) {

    @PostMapping("/{taskName}")
    fun createTask(
        @PathVariable taskName: String,
        @RequestParam projectId: UUID,
        @RequestParam creatorId: UUID
    ): TaskCreatedEvent {
        return taskService.createTask(taskName, projectId, creatorId)
    }

    @PostMapping("/{taskId}/executor")
    fun addExecutor(
        @PathVariable taskId: UUID,
        @RequestParam initiatorUserId: UUID,
        @RequestParam addUserId: UUID
    ): TaskExecutorAddedEvent {
        return taskService.addExecutor(taskId, initiatorUserId, addUserId)
    }

    @PutMapping("/{newTaskName}")
    fun changeTaskName(
        @PathVariable newTaskName: String,
        @RequestParam userId: UUID,
        @RequestParam taskId: UUID
    ): TaskNameUpdatedEvent {
        return taskService.changeTaskName(newTaskName, userId, taskId)
    }

    @PutMapping("/{taskId}/status")
    fun changeStatus(
        @PathVariable taskId: UUID,
        @RequestParam statusName: String,
        @RequestParam statusColor: String,
        @RequestParam userId: UUID
    ): TaskStatusChangedEvent {
        return taskService.changeStatus(taskId, statusName, statusColor, userId)
    }
}
