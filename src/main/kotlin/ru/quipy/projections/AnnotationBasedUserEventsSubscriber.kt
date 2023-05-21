package ru.quipy.projections

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import ru.quipy.api.UserAggregate
import ru.quipy.api.UserCreatedEvent
import ru.quipy.streams.annotation.AggregateSubscriber
import ru.quipy.streams.annotation.SubscribeEvent

@Service
@AggregateSubscriber(
    aggregateClass = UserAggregate::class, subscriberName = "user-subs-stream"
)
class AnnotationBasedUserEventsSubscriber(
    val webClient: WebClient
) {

    val logger: Logger = LoggerFactory.getLogger(AnnotationBasedUserEventsSubscriber::class.java)


    @SubscribeEvent
    fun userCreatedSubscriber(event: UserCreatedEvent) {
        logger.info("User created: {}", event.userName)

        val stringMono: String? = webClient.post()
            .uri("/users")
            .body(
                BodyInserters
                    .fromFormData("userId", event.userId.toString())
                    .with("userName", event.userName)
            )
            .retrieve()
            .onStatus(HttpStatus::isError) {
                it.bodyToMono(String::class.java)
                    .flatMap {
                            err -> Mono.error(RuntimeException(err))
                    }
            }

            .bodyToMono(String::class.java).block()

        logger.info("Projection - User created: {}", stringMono)

    }

}
