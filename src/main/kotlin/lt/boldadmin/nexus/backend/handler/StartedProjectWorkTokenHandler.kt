package lt.boldadmin.nexus.backend.handler

import lt.boldadmin.nexus.api.service.StartedProjectWorkTokenService
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono

open class StartedProjectWorkTokenHandler(private val service: StartedProjectWorkTokenService) {

    open fun generateAndStore(req: ServerRequest): Mono<ServerResponse> =
        req.bodyToMono<String>()
            .doOnNext { service.generateAndStore(it) }
            .flatMap { ok().build() }

    open fun findTokenById(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(service.findTokenById(req.pathVariable("projectId"))))

    open fun findIdByToken(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(service.findIdByToken(req.pathVariable("token"))))

    open fun findProjectByToken(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(service.findProjectByToken(req.pathVariable("token"))))

    open fun findWorkingCollaboratorIdsByToken(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(service.findWorkingCollaboratorIdsByToken(req.pathVariable("token"))))

    open fun existsById(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(service.existsById(req.pathVariable("projectId"))))

    open fun deleteById(req: ServerRequest): Mono<ServerResponse> =
        req.bodyToMono<String>()
            .doOnNext { service.deleteById(it) }
            .flatMap { ok().build() }
}