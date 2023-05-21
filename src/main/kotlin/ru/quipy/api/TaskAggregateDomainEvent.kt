package ru.quipy.api

import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event
import ru.quipy.logic.Status
import java.util.*

const val TASK_CREATED_EVENT = "TASK_CREATED_EVENT"
const val TASK_NAME_CHANGED_EVENT = "TASK_NAME_CHANGED_EVENT"
const val TASK_EXECUTOR_ADDED_EVENT = "TASK_EXECUTOR_ADDED_EVENT"
const val TASK_STATUSES_CHANGED_EVENT = "TASK_STATUSES_CHANGED_EVENT"

@DomainEvent(name = TASK_CREATED_EVENT)
class TaskCreatedEvent(
    val taskId: UUID,
    val taskName: String,
    val status: Status,
    val projectId: UUID,
    createdAt: Long = System.currentTimeMillis(),
) : Event<TaskAggregate>(
    name = TASK_CREATED_EVENT,
    createdAt = createdAt,
)

@DomainEvent(name = TASK_NAME_CHANGED_EVENT)
class TaskNameUpdatedEvent(
    val taskId: UUID,
    val taskNewName: String,
    val userId: UUID,
    createdAt: Long = System.currentTimeMillis(),
) : Event<TaskAggregate>(
    name = TASK_NAME_CHANGED_EVENT,
    createdAt = createdAt,
)

@DomainEvent(name = TASK_EXECUTOR_ADDED_EVENT)
class TaskExecutorAddedEvent(
    val taskId: UUID,
    val addUserId: UUID,
    val initiatorUserId: UUID,
    createdAt: Long = System.currentTimeMillis(),
) : Event<TaskAggregate>(
    name = TASK_EXECUTOR_ADDED_EVENT,
    createdAt = createdAt,
)

@DomainEvent(name = TASK_STATUSES_CHANGED_EVENT)
class TaskStatusChangedEvent(
    val taskId: UUID,
    val status: Status,
    val userId: UUID,
    createdAt: Long = System.currentTimeMillis(),
) : Event<TaskAggregate>(
    name = TASK_STATUSES_CHANGED_EVENT,
    createdAt = createdAt,
)
