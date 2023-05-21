package ru.quipy.service

import org.springframework.stereotype.Service
import ru.quipy.api.*
import ru.quipy.core.EventSourcingService
import ru.quipy.logic.*
import java.util.*

@Service
class ProjectService(
    val projectEsService: EventSourcingService<UUID, ProjectAggregate, ProjectAggregateState>,
    val projectionsService: ProjectionsService
) {
    fun create(randomUUID: UUID?, projectTitle: String, creatorId: UUID): ProjectCreatedEvent {
        return projectEsService.create { it.create(UUID.randomUUID(), projectTitle, creatorId) }
    }

    fun addMember(projectId: UUID, initiatorUserId: UUID, addUserId: UUID): MemberAddedEvent {
        val members = projectionsService.getProjectMembersByProjectId(projectId)
        if (members != null) {
            for (member in members) {
                if (member == initiatorUserId) {
                    return projectEsService.update(projectId) {it.addMember(addUserId)}
                }
            }
        }
        throw IllegalArgumentException("Initiator User with id=$initiatorUserId is not belong to the project")
    }

    fun addStatus(projectId: UUID, statusName: String, statusColor: String): StatusCreatedEvent {
        val statuses = projectionsService.getProjectStatusesByProjectId(projectId)
        for (status in statuses) {
            if (status.statusName == statusName && status.statusColor == statusColor) {
                throw IllegalArgumentException("There is the same Status in the project: $statusName:$statusColor")
            }
        }
        return projectEsService.update(projectId) {it.addStatus(statusName, statusColor)}

    }

    fun deleteStatus(projectId: UUID, statusName: String, statusColor: String): StatusDeletedEvent {
        val statuses = projectionsService.getProjectStatusesByProjectId(projectId)
        for (status in statuses) {
            if (statusName == STATUS_DEFAULT_NAME && statusColor == STATUS_DEFAULT_COLOR) {
                throw IllegalArgumentException("Default Status can not be deleted: $statusName:$statusColor")
            }
            if (status.statusName == statusName && status.statusColor == statusColor) {
                return projectEsService.update(projectId) {it.deleteStatus(statusName, statusColor)}
            }
        }
        throw IllegalArgumentException("There is no Status to delete: $statusName:$statusColor")
    }


}
