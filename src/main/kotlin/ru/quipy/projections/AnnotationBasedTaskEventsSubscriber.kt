package ru.quipy.projections

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import ru.quipy.api.*
import ru.quipy.streams.annotation.AggregateSubscriber
import ru.quipy.streams.annotation.SubscribeEvent

@Service
@AggregateSubscriber(
    aggregateClass = TaskAggregate::class, subscriberName = "task-subs-stream"
)
class AnnotationBasedTaskEventsSubscriber (
    val webClient: WebClient
) {

    val logger: Logger = LoggerFactory.getLogger(AnnotationBasedTaskEventsSubscriber::class.java)

    @SubscribeEvent
    fun taskCreatedSubscriber(event: TaskCreatedEvent) {
        val stringMono: String? = webClient.post()
            .uri("/tasks")
            .body(
                BodyInserters
                    .fromFormData("taskId", event.taskId.toString())
                    .with("projectId", event.projectId.toString())
                    .with("taskName", event.taskName)
                    .with("statusName", event.status.statusName)
                    .with("statusColor", event.status.statusColor)
            )
            .retrieve()
            .bodyToMono(String::class.java)
            .block()

        logger.info("Projection - Task created: {}", stringMono)
    }

    @SubscribeEvent
    fun taskNameUpdatedSubscriber(event: TaskNameUpdatedEvent) {
        val stringMono: String? = webClient.put()
            .uri("/tasks/${event.taskId}/name")
            .body(
                BodyInserters
                    .fromFormData("taskName", event.taskNewName)
            )
            .retrieve()
            .bodyToMono(String::class.java)
            .block()

        logger.info("Projection - Task. Name updated: {}", stringMono)
    }

    @SubscribeEvent
    fun taskExecutorAddedSubscriber(event: TaskExecutorAddedEvent) {
        val stringMono: String? = webClient.post()
            .uri("/tasks/${event.taskId}/executors")
            .body(
                BodyInserters
                    .fromFormData("userId", event.addUserId.toString())
            )
            .retrieve()
            .bodyToMono(String::class.java)
            .block()

        logger.info("Projection - Task got an executor: {}", stringMono)
    }

    @SubscribeEvent
    fun taskStatusChangedSubscriber(event: TaskStatusChangedEvent) {
        logger.info("Task has changed Status: name:{}, color:{}", event.status.statusName, event.status.statusColor)
        val stringMono: String? = webClient.put()
            .uri("/tasks/${event.taskId}/status")
            .body(
                BodyInserters
                    .fromFormData("statusName", event.status.statusName)
                    .with("statusColor", event.status.statusColor)
            )
            .retrieve()
            .bodyToMono(String::class.java)
            .block()

        logger.info("Projection - Task. Status updated: {}", stringMono)
    }

}
