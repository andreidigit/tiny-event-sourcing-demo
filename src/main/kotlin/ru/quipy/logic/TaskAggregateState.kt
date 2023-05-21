package ru.quipy.logic

import ru.quipy.api.*
import ru.quipy.core.annotations.StateTransitionFunc
import ru.quipy.domain.AggregateState
import java.util.*

// Service's business logic
class TaskAggregateState : AggregateState<UUID, TaskAggregate> {
    private lateinit var taskId: UUID
    var createdAt: Long = System.currentTimeMillis()
    var updatedAt: Long = System.currentTimeMillis()

    lateinit var projectId: UUID
    lateinit var taskName: String
    lateinit var status: Status
    var taskExecutors = mutableSetOf<UUID>()

    override fun getId() = taskId

    // State transition functions which is represented by the class member function
    @StateTransitionFunc
    fun taskCreatedApply(event: TaskCreatedEvent) {
        taskId = event.taskId
        taskName = event.taskName
        status = event.status
        projectId = event.projectId
        updatedAt = createdAt
    }

    @StateTransitionFunc
    fun taskNameUpdatedApply(event: TaskNameUpdatedEvent) {
        taskName = event.taskNewName
        updatedAt = createdAt
    }

    @StateTransitionFunc
    fun taskExecutorAddedApply(event: TaskExecutorAddedEvent) {
        taskExecutors.add(event.addUserId)
        updatedAt = createdAt
    }

    @StateTransitionFunc
    fun taskStatusChangedApply(event: TaskStatusChangedEvent) {
        status = event.status
        updatedAt = createdAt
    }
}
