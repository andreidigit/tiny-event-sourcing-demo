package com.quipy.projection.entities

import com.fasterxml.jackson.annotation.*
import org.hibernate.annotations.Type
import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "user_tm")
@JsonIgnoreProperties(ignoreUnknown = true)
data class User(
    @Id @Column(unique = true, updatable = false) @Type(type = "org.hibernate.type.UUIDCharType")
    var userId: UUID,
    var userName: String,

    /* @OneToMany(fetch = FetchType.LAZY)
     var projects: List<Project>? = null,*/
)


/**
 * ***** Project
 */
@Entity
data class Project(

    @Id @Column(unique = true, updatable = false) @Type(type = "org.hibernate.type.UUIDCharType")
    var projectId: UUID,

    var projectTitle: String,

    var creator: UUID,
)

class ProjectUserId(val projectId: UUID? = null, val userId: UUID? = null) : Serializable

@Entity
@IdClass(ProjectUserId::class)
data class ProjectsUsers(
    @Id @Type(type = "org.hibernate.type.UUIDCharType") val projectId: UUID,
    @Id @Type(type = "org.hibernate.type.UUIDCharType") val userId: UUID,
)


/**
 * ***** Status
 */
class StatusId(val statusId: Long? = null, val projectId: UUID? = null) : Serializable

@Entity
@IdClass(StatusId::class)
@SequenceGenerator(name="OPTION_VALUE", sequenceName="OPTION_VALUE_GENERATOR")
data class Status(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="OPTION_VALUE") var statusId: Long? = null,
    @Id @Type(type = "org.hibernate.type.UUIDCharType") var projectId: UUID,
    val statusName: String,
    val statusColor: String,
)


/**
 * ***** Task
 */
class TaskUserId(val taskId: UUID? = null, val userId: UUID? = null) : Serializable

@Entity
@IdClass(TaskUserId::class)
data class TasksUsers(
    @Id @Type(type = "org.hibernate.type.UUIDCharType") val taskId: UUID,
    @Id @Type(type = "org.hibernate.type.UUIDCharType") val userId: UUID,
)

@Entity
class Task(
    @Id @Type(type = "org.hibernate.type.UUIDCharType") var taskId: UUID,
    val projectId: UUID,
    var taskName: String,

    var statusName: String,
    var statusColor: String,
)
