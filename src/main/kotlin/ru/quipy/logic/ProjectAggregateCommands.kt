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
    )
}

/*
fun ProjectAggregateState.addTask(name: String): TaskCreatedEvent {
    return TaskCreatedEvent(projectId = this.getId(), taskId = UUID.randomUUID(), taskName = name)
}

fun ProjectAggregateState.createTag(name: String): TagCreatedEvent {
    if (projectTags.values.any { it.name == name }) {
        throw IllegalArgumentException("Tag already exists: $name")
    }
    return TagCreatedEvent(projectId = this.getId(), tagId = UUID.randomUUID(), tagName = name)
}

fun ProjectAggregateState.assignTagToTask(tagId: UUID, taskId: UUID): TagAssignedToTaskEvent {
    if (!projectTags.containsKey(tagId)) {
        throw IllegalArgumentException("Tag doesn't exists: $tagId")
    }

    if (!tasks.containsKey(taskId)) {
        throw IllegalArgumentException("Task doesn't exists: $taskId")
    }

    return TagAssignedToTaskEvent(projectId = this.getId(), tagId = tagId, taskId = taskId)
}
*/

fun ProjectAggregateState.addStatus(statusName: String, statusColor: String): StatusCreatedEvent {
    for (status in this.statuses) {
        if (status.statusName == statusName && status.statusColor == statusColor) {
            throw IllegalArgumentException("There is the same Status in the project: $statusName:$statusColor")
        }
    }
    return StatusCreatedEvent(projectId = this.getId(), statusName = statusName, statusColor = statusColor)
}

fun ProjectAggregateState.deleteStatus(statusName: String, statusColor: String): StatusDeletedEvent {
    for (status in this.statuses) {
        if (statusName == STATUS_DEFAULT_NAME && statusColor == STATUS_DEFAULT_COLOR){
            throw IllegalArgumentException("Default Status can not be deleted: $statusName:$statusColor")
        }
        if (status.statusName == statusName && status.statusColor == statusColor) {
            return StatusDeletedEvent(projectId = this.getId(), statusName = statusName, statusColor = statusColor)
        }
    }
    throw IllegalArgumentException("There is no Status to delete: $statusName:$statusColor")
}

fun ProjectAggregateState.addMember(initiatorUserId: UUID, addUserId: UUID): MemberAddedEvent {
    for (member in this.projectMembers) {
        if (member == initiatorUserId) {
            return MemberAddedEvent(
                projectId = this.getId(),
                initiatorUserId = initiatorUserId,
                addUserId = addUserId
            )
        }
    }
    throw IllegalArgumentException("Initiator User with id=$initiatorUserId is not belong to the project")
}
