package ru.quipy.logic

import ru.quipy.api.*
import java.util.*


// Commands : takes something -> returns event
// Here the commands are represented by extension functions, but also can be the class member functions

fun TaskAggregateState.create(id: UUID, taskName: String, projectId: UUID): TaskCreatedEvent {
    val status = Status(statusName = STATUS_DEFAULT_NAME, statusColor = STATUS_DEFAULT_COLOR)
    return TaskCreatedEvent(
        taskId = id,
        taskName = taskName,
        projectId = projectId,
        status = status
    )
}

fun TaskAggregateState.updateName(taskNewName: String, userId: UUID): TaskNameUpdatedEvent {
    return TaskNameUpdatedEvent(
        taskNewName = taskNewName,
        userId = userId
    )
}

fun TaskAggregateState.addExecutor(addUserId: UUID, initiatorUserId: UUID): TaskExecutorAddedEvent {
    return TaskExecutorAddedEvent(
        addUserId = addUserId,
        initiatorUserId = initiatorUserId
    )
}

fun TaskAggregateState.setStatus(status: Status, userId: UUID): TaskStatusChangedEvent{
    return TaskStatusChangedEvent(
        status = status,
        userId = userId
    )
}
