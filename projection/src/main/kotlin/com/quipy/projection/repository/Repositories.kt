package com.quipy.projection.repository

import com.quipy.projection.entities.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, UUID> {
    fun findByUserName(userName: String): User?
    fun findOneByUserId(userId: UUID?): User?
}

@Repository
interface ProjectRepository : JpaRepository<Project, UUID> {
}

@Repository
interface ProjectsUsersRepository : JpaRepository<ProjectsUsers, ProjectUserId> {
    fun findByProjectId(projectId: UUID): List<ProjectsUsers>
}

@Repository
interface StatusRepository : JpaRepository<Status, StatusId> {
    fun findByProjectId(projectId: UUID): List<Status>
}

@Repository
interface TaskRepository : JpaRepository<Task, UUID> {
}

@Repository
interface TasksUsersRepository : JpaRepository<TasksUsers, TaskUserId> {
    fun findByTaskId(taskId: UUID): List<TasksUsers>
}
