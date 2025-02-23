package ru.quipy.logic

import ru.quipy.api.*
import ru.quipy.core.annotations.StateTransitionFunc
import ru.quipy.domain.AggregateState
import java.util.*

// Service's business logic
class ProjectAggregateState : AggregateState<UUID, ProjectAggregate> {
    private lateinit var projectId: UUID
    var createdAt: Long = System.currentTimeMillis()
    var updatedAt: Long = System.currentTimeMillis()

    lateinit var projectTitle: String
    lateinit var creatorId: UUID
    var tasks = mutableMapOf<UUID, TaskEntity>()
    var projectTags = mutableMapOf<UUID, TagEntity>()
    var statuses = mutableListOf<Status>()
    var projectMembers = mutableSetOf<UUID>()

    override fun getId() = projectId

    // State transition functions which is represented by the class member function
    @StateTransitionFunc
    fun projectCreatedApply(event: ProjectCreatedEvent) {
        projectId = event.projectId
        projectTitle = event.title
        creatorId = event.creatorId
        projectMembers.add(event.creatorId)
        statuses.add(Status("CREATED", "#000000"))
        updatedAt = createdAt
    }

/*    @StateTransitionFunc
    fun tagCreatedApply(event: TagCreatedEvent) {
        projectTags[event.tagId] = TagEntity(event.tagId, event.tagName)
        updatedAt = createdAt
    }

    @StateTransitionFunc
    fun taskCreatedApply(event: TaskCreatedEvent) {
        tasks[event.taskId] = TaskEntity(event.taskId, event.taskName, mutableSetOf())
        updatedAt = createdAt
    }*/

    @StateTransitionFunc
    fun statusCreatedApply(event: StatusCreatedEvent) {
        statuses.add(Status(event.statusName, event.statusColor))
        updatedAt = createdAt
    }

    @StateTransitionFunc
    fun statusDeletedApply(event: StatusDeletedEvent) {
        statuses.remove(Status(event.statusName, event.statusColor))
        updatedAt = createdAt
    }

    @StateTransitionFunc
    fun addMemberApply(event: MemberAddedEvent) {
        projectMembers.add(event.addUserId)
        updatedAt = createdAt
    }
}

data class TaskEntity(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val tagsAssigned: MutableSet<UUID>
)

data class TagEntity(
    val id: UUID = UUID.randomUUID(),
    val name: String
)

const val STATUS_DEFAULT_NAME = "CREATED"
const val STATUS_DEFAULT_COLOR = "#000000"
data class Status(
    val statusName: String,
    val statusColor: String
)

/**
 * Demonstrates that the transition functions might be representer by "extension" functions, not only class members functions

@StateTransitionFunc
fun ProjectAggregateState.tagAssignedApply(event: TagAssignedToTaskEvent) {
    tasks[event.taskId]?.tagsAssigned?.add(event.tagId)
        ?: throw IllegalArgumentException("No such task: ${event.taskId}")
    updatedAt = createdAt
}

*/
