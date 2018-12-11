package lt.boldadmin.nexus.backend.handler

import lt.boldadmin.nexus.api.service.ProjectService
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono

open class ProjectHandler(
    private val projectService: ProjectService
) {
    open fun createWithDefaults(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(projectService.createWithDefaults(req.pathVariable("userId"))))

    open fun getById(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(projectService.getById(req.pathVariable("projectId"))))

    open fun update(req: ServerRequest): Mono<ServerResponse> =
        req.bodyToMono<String>()
            .doOnNext { projectService.update(req.pathVariable("projectId"), req.pathVariable("attributeName"), it) }
            .flatMap { ok().build() }

    open fun updateOrderNumber(req: ServerRequest): Mono<ServerResponse> =
        req.bodyToMono<Short>()
            .doOnNext { projectService.updateOrderNumber(req.pathVariable("projectId"), it) }
            .flatMap { ok().build() }

}