package com.quipy.projection.controller

import com.quipy.projection.entities.Task
import com.quipy.projection.service.TaskService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/tasks")
class TaskController(
    val taskService: TaskService
) {

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    fun createTask(
        @RequestParam taskId: UUID,
        @RequestParam projectId: UUID,
        @RequestParam taskName: String,
        @RequestParam statusName: String,
        @RequestParam statusColor: String,
    ): Task {
        return taskService.create(taskId, projectId, taskName, statusName, statusColor)
    }

    @PutMapping("/{taskId}/name")
    fun updateTaskName(
        @PathVariable taskId: UUID,
        @RequestParam taskName: String,
    ): Task {
        return taskService.updateName(taskId, taskName)
    }

    @PutMapping("/{taskId}/status")
    fun updateTaskStatus(
        @PathVariable taskId: UUID,
        @RequestParam statusName: String,
        @RequestParam statusColor: String,
    ): Task {
        return taskService.updateStatus(taskId, statusName, statusColor)
    }

    @PostMapping("/{taskId}/executors")
    @ResponseStatus(HttpStatus.OK)
    fun addExecutor(
        @PathVariable taskId: UUID,
        @RequestParam userId: UUID
    ){
        taskService.addExecutor(taskId, userId)
    }

    @GetMapping("{taskId}")
    fun getTask(
        @PathVariable taskId: UUID
    ): Task {
        return taskService.getById(taskId)
    }

    @GetMapping("/{taskId}/executors")
    fun getExecutors(
        @PathVariable taskId: UUID
    ): List<UUID>{
        return taskService.getExecutors(taskId)
    }

    @GetMapping("/{taskId}/project-members")
    fun getMembers(
        @PathVariable taskId: UUID
    ): List<UUID>{
        return taskService.getMembers(taskId)
    }

    @GetMapping("/{taskId}/statuses")
    fun getStatuses(
        @PathVariable taskId: UUID
    ): List<String>{
        return taskService.getStatuses(taskId)
    }
}
