package lt.boldadmin.nexus.backend.handler

import lt.boldadmin.nexus.api.service.collaborator.CollaboratorCoordinatesService
import lt.boldadmin.nexus.api.service.collaborator.CollaboratorService
import lt.boldadmin.nexus.api.type.entity.collaborator.Collaborator
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Mono

open class CollaboratorHandler(
    private val collaboratorService: CollaboratorService,
    private val collaboratorCoordinatesService: CollaboratorCoordinatesService
) {

    open fun existsById(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(collaboratorService.existsById(req.pathVariable("collaboratorId"))))

    open fun existsByMobileNumber(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(collaboratorService.existsByMobileNumber(req.pathVariable("mobileNumber"))))

    open fun save(req: ServerRequest): Mono<ServerResponse> =
        req.bodyToMono<Collaborator>()
            .doOnNext { collaboratorService.save(it) }
            .flatMap { ok().build() }

    open fun createWithDefaults(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(collaboratorService.createWithDefaults()))

    open fun getById(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(collaboratorService.getById(req.pathVariable("collaboratorId"))))

    open fun getCoordinates(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(collaboratorCoordinatesService.getByCollaboratorId(req.pathVariable("collaboratorId"))))

    open fun getByMobileNumber(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(collaboratorService.getByMobileNumber(req.pathVariable("mobileNumber"))))

    open fun update(req: ServerRequest): Mono<ServerResponse> =
        req.bodyToMono<String>()
            .defaultIfEmpty("")
            .doOnNext {
                collaboratorService.update(req.pathVariable("collaboratorId"), req.pathVariable("attributeName"), it)
            }.flatMap { ok().build() }

    open fun updateOrderNumber(req: ServerRequest): Mono<ServerResponse> =
        req.bodyToMono<String>()
            .map { it.toShort() }
            .doOnNext { collaboratorService.updateOrderNumber(req.pathVariable("collaboratorId"), it) }
            .flatMap { ok().build() }

}
