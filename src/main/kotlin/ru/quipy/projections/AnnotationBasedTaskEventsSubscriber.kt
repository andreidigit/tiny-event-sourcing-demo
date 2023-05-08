package ru.quipy.projections

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.quipy.api.*
import ru.quipy.streams.annotation.AggregateSubscriber
import ru.quipy.streams.annotation.SubscribeEvent

@Service
@AggregateSubscriber(
    aggregateClass = UserAggregate::class, subscriberName = "task-subs-stream"
)
class AnnotationBasedTaskEventsSubscriber {

    val logger: Logger = LoggerFactory.getLogger(AnnotationBasedTaskEventsSubscriber::class.java)

    @SubscribeEvent
    fun taskCreatedSubscriber(event: TaskCreatedEvent) {
        logger.info("Task created: {}", event.taskName)
    }

    @SubscribeEvent
    fun taskNameUpdatedSubscriber(event: TaskNameUpdatedEvent) {
        logger.info("Task got a new name: {}", event.taskNewName)
    }

    @SubscribeEvent
    fun taskExecutorAddedSubscriber(event: TaskExecutorAddedEvent) {
        logger.info("Task got executor with id: {}", event.addUserId)
    }

    @SubscribeEvent
    fun taskStatusChangedSubscriber(event: TaskStatusChangedEvent) {
        logger.info("Task has changed Status: name:{}, color:{}", event.status.statusName, event.status.statusColor)
    }

}
