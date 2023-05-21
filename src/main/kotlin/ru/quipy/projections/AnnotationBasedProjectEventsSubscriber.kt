package ru.quipy.projections

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import ru.quipy.api.*
import ru.quipy.streams.annotation.AggregateSubscriber
import ru.quipy.streams.annotation.SubscribeEvent

@Service
@AggregateSubscriber(
    aggregateClass = ProjectAggregate::class, subscriberName = "demo-subs-stream"
)
class AnnotationBasedProjectEventsSubscriber (
    val webClient: WebClient
) {

    val logger: Logger = LoggerFactory.getLogger(AnnotationBasedProjectEventsSubscriber::class.java)

    @SubscribeEvent
    fun taskCreatedSubscriber(event: ProjectCreatedEvent) {
        val stringMono: String? = webClient.post()
            .uri("/projects")
            .body(
                BodyInserters
                    .fromFormData("projectId", event.projectId.toString())
                    .with("projectTitle", event.title)
                    .with("creatorId", event.creatorId.toString())
                    .with("statusName", event.statusName)
                    .with("statusColor", event.statusColor)
            )
            .retrieve()
            .bodyToMono(String::class.java)
            .block()

        logger.info("Projection - Project. A project created: {}", stringMono)
    }

    @SubscribeEvent
    fun memberAddedSubscriber(event: MemberAddedEvent) {
        val stringMono: String? = webClient.post()
            .uri("/projects/${event.projectId}/members")
            .body(
                BodyInserters
                    .fromFormData("userId", event.addUserId.toString())
            )
            .retrieve()
            .bodyToMono(String::class.java)
            .block()

        logger.info("Projection - Project. A member has added: {}", stringMono)
    }

    @SubscribeEvent
    fun statusCreatedSubscriber(event: StatusCreatedEvent) {
        val stringMono: String? = webClient.post()
            .uri("/projects/${event.projectId}/status")
            .body(
                BodyInserters
                    .fromFormData("statusName", event.statusName)
                    .with("statusColor", event.statusColor)
            )
            .retrieve()
            .bodyToMono(String::class.java)
            .block()

        logger.info("Projection - Project. A status has added: {}", stringMono)
    }

    @SubscribeEvent
    fun statusDeletedSubscriber(event: StatusDeletedEvent) {
        val stringMono: String? = webClient.method(HttpMethod.DELETE)
            .uri("/projects/${event.projectId}/status")
            .body(
                BodyInserters
                    .fromFormData("statusName", event.statusName)
                    .with("statusColor", event.statusColor)
            )
            .retrieve()
            .bodyToMono(String::class.java)
            .block()

        logger.info("Projection - Project. A status deleted: {}", stringMono)
    }
}
