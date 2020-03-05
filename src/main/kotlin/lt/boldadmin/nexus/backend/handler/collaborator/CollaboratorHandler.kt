package lt.boldadmin.nexus.backend.handler.collaborator

import lt.boldadmin.nexus.api.service.collaborator.CollaboratorService
import lt.boldadmin.nexus.api.type.entity.Collaborator
import lt.boldadmin.nexus.api.type.valueobject.time.DayMinuteInterval
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Mono
import java.util.*

open class CollaboratorHandler(private val service: CollaboratorService) {

    open fun existsById(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(service.existsById(req.pathVariable("collaboratorId"))))

    open fun existsByMobileNumber(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(service.existsByMobileNumber(req.pathVariable("mobileNumber"))))

    open fun save(req: ServerRequest): Mono<ServerResponse> =
        req.bodyToMono<Collaborator>()
            .doOnNext { service.save(it) }
            .flatMap { ok().build() }

    open fun createWithDefaults(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(service.createWithDefaults()))

    open fun getById(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(service.getById(req.pathVariable("collaboratorId"))))

    open fun getByMobileNumber(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(service.getByMobileNumber(req.pathVariable("mobileNumber"))))

    open fun update(req: ServerRequest): Mono<ServerResponse> =
        req.bodyToMono<String>()
            .defaultIfEmpty("")
            .doOnNext {
                service.update(req.pathVariable("collaboratorId"), req.pathVariable("attributeName"), it)
            }.flatMap { ok().build() }

    open fun updateWorkWeek(req: ServerRequest): Mono<ServerResponse> =
        req.bodyToMono<SortedSet<DayMinuteInterval>>()
            .doOnNext {
                service.update(req.pathVariable("collaboratorId"), it)
            }.flatMap { ok().build() }

    open fun updateOrderNumber(req: ServerRequest): Mono<ServerResponse> =
        req.bodyToMono<String>()
            .map { it.toShort() }
            .doOnNext { service.updateOrderNumber(req.pathVariable("collaboratorId"), it) }
            .flatMap { ok().build() }

}
