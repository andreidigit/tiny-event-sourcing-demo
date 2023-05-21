package ru.quipy.logic

import ru.quipy.api.*
import java.util.*


// Commands : takes something -> returns event
// Here the commands are represented by extension functions, but also can be the class member functions

fun ProjectAggregateState.create(id: UUID, title: String, creatorId: UUID): ProjectCreatedEvent {
    return ProjectCreatedEvent(
        projectId = id,
        title = title,
        creatorId = creatorId,
        statusName = STATUS_DEFAULT_NAME,
        statusColor = STATUS_DEFAULT_COLOR
    )
}

fun ProjectAggregateState.addStatus(statusName: String, statusColor: String): StatusCreatedEvent {
    return StatusCreatedEvent(
        projectId = this.getId(),
        statusName = statusName,
        statusColor = statusColor
    )
}

fun ProjectAggregateState.deleteStatus(statusName: String, statusColor: String): StatusDeletedEvent {
    return StatusDeletedEvent(
        projectId = this.getId(),
        statusName = statusName,
        statusColor = statusColor
    )
}

fun ProjectAggregateState.addMember(addUserId: UUID): MemberAddedEvent {
    return MemberAddedEvent(
        projectId = this.getId(),
        addUserId = addUserId
    )
}
