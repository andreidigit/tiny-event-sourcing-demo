package com.quipy.projection.service

import com.quipy.projection.entities.Task
import com.quipy.projection.entities.TasksUsers
import com.quipy.projection.repository.ProjectsUsersRepository
import com.quipy.projection.repository.StatusRepository
import com.quipy.projection.repository.TaskRepository
import com.quipy.projection.repository.TasksUsersRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class TaskService(
    val taskRepository: TaskRepository,
    val tasksUsersRepository: TasksUsersRepository,
    val projectsUsersRepository: ProjectsUsersRepository,
    val statusRepository: StatusRepository
) {
    fun create(taskId: UUID, projectId: UUID, taskName: String, statusName: String, statusColor: String): Task {
        return taskRepository.save(Task(taskId, projectId, taskName, statusName, statusColor))
    }

    fun updateName(taskId: UUID, taskName: String): Task {
        val task = taskRepository.getReferenceById(taskId)
        task.taskName=taskName
        return taskRepository.save(task)
    }

    fun updateStatus(taskId: UUID, statusName: String, statusColor: String): Task {
        val task = taskRepository.getReferenceById(taskId)
        task.statusName = statusName
        task.statusColor = statusColor
        return taskRepository.save(task)
    }

    fun addExecutor(taskId: UUID, userId: UUID) {
        tasksUsersRepository.save(TasksUsers(taskId, userId))
    }

    fun getById(taskId: UUID): Task {
        return taskRepository.getReferenceById(taskId)
    }

    fun getExecutors(taskId: UUID): List<UUID> {
        return tasksUsersRepository.findByTaskId(taskId).map { it.userId }
    }

    fun getMembers(taskId: UUID): List<UUID> {
        val projectId = taskRepository.getReferenceById(taskId).projectId
        return projectsUsersRepository.findByProjectId(projectId).map { it.userId }
    }

    fun getStatuses(taskId: UUID): List<String> {
        val projectId = taskRepository.getReferenceById(taskId).projectId
        return statusRepository.findByProjectId(projectId).map { "${it.statusName}:${it.statusColor}" }
    }
}
