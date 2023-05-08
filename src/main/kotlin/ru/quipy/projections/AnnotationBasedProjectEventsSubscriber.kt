package ru.quipy.projections

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.quipy.api.*
import ru.quipy.streams.annotation.AggregateSubscriber
import ru.quipy.streams.annotation.SubscribeEvent

@Service
@AggregateSubscriber(
    aggregateClass = ProjectAggregate::class, subscriberName = "demo-subs-stream"
)
class AnnotationBasedProjectEventsSubscriber {

    val logger: Logger = LoggerFactory.getLogger(AnnotationBasedProjectEventsSubscriber::class.java)

/*    @SubscribeEvent
    fun taskCreatedSubscriber(event: TaskCreatedEvent) {
        logger.info("Task created: {}", event.taskName)
    }

    @SubscribeEvent
    fun tagCreatedSubscriber(event: TagCreatedEvent) {
        logger.info("Tag created: {}", event.tagName)
    }*/

    @SubscribeEvent
    fun memberAddedSubscriber(event: MemberAddedEvent) {
        logger.info("Another member has added: id={}", event.addUserId)
    }

    @SubscribeEvent
    fun statusCreatedSubscriber(event: StatusCreatedEvent) {
        logger.info("Status created: {}:{}", event.statusName, event.statusColor)
    }

    @SubscribeEvent
    fun statusDeletedSubscriber(event: StatusDeletedEvent) {
        logger.info("Status deleted: {}:{}", event.statusName, event.statusColor)
    }
}
