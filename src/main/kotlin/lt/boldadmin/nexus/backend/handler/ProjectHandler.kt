package lt.boldadmin.nexus.backend.handler

import lt.boldadmin.nexus.api.service.ProjectService
import lt.boldadmin.nexus.api.type.valueobject.Location
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Mono

open class ProjectHandler(private val projectService: ProjectService) {

    open fun createWithDefaults(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(projectService.createWithDefaults(req.pathVariable("userId"))))

    open fun getById(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(projectService.getById(req.pathVariable("projectId"))))

    open fun update(req: ServerRequest): Mono<ServerResponse> =
        req.bodyToMono<String>()
            .defaultIfEmpty("")
            .doOnNext { projectService.update(req.pathVariable("projectId"), req.pathVariable("attributeName"), it) }
            .flatMap { ok().build() }

    open fun updateOrderNumber(req: ServerRequest): Mono<ServerResponse> =
        req.bodyToMono<String>()
            .map { it.toShort() }
            .doOnNext { projectService.updateOrderNumber(req.pathVariable("projectId"), it) }
            .flatMap { ok().build() }

    open fun updateLocation(req: ServerRequest): Mono<ServerResponse> =
        req.bodyToMono<Location>()
            .doOnNext { projectService.updateLocation(req.pathVariable("projectId"), it) }
            .flatMap { ok().build() }

    open fun deleteLocation(req: ServerRequest): Mono<ServerResponse> =
        Mono.just(projectService.deleteLocation(req.pathVariable("projectId")))
            .flatMap { ok().build() }
}