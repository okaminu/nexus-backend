package lt.boldadmin.nexus.backend.handler

import lt.boldadmin.nexus.api.service.UserService
import lt.boldadmin.nexus.api.type.entity.User
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono

open class UserHandler(
    private val userService: UserService
) {

    open fun save(req: ServerRequest): Mono<ServerResponse> =
        req.bodyToMono<User>()
            .doOnNext { userService.save(it) }
            .flatMap { ok().build() }

    open fun createWithDefaults(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(userService.createWithDefaults()))

    open fun getById(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(userService.getById(req.pathVariable("userId"))))

    open fun getByEmail(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(userService.getByEmail(req.pathVariable("email"))?: ""))

    open fun getByProjectId(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(userService.getByProjectId(req.pathVariable("projectId"))))

    open fun doesUserHaveCustomer(req: ServerRequest): Mono<ServerResponse> =
        ok().body(
                Mono.just(userService.doesUserHaveCustomer(
                        req.pathVariable("userId"), req.pathVariable("customerId"))))

    open fun doesUserHaveProject(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(userService.doesUserHaveProject(
                req.pathVariable("userId"),
                req.pathVariable("projectId")
        )))

    open fun doesUserHaveCollaborator(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(userService.doesUserHaveCollaborator(
                req.pathVariable("userId"),
                req.pathVariable("collaboratorId")
        )))

    open fun isProjectNameUnique(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(userService.isProjectNameUnique(
                req.pathVariable("projectName"),
                req.pathVariable("projectId"),
                req.pathVariable("userId")
        )))
}