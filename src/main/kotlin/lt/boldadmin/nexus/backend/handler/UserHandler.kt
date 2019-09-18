package lt.boldadmin.nexus.backend.handler

import lt.boldadmin.nexus.api.service.UserService
import lt.boldadmin.nexus.api.type.entity.User
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Mono
import reactor.core.publisher.Mono.just

open class UserHandler(private val userService: UserService) {

    open fun save(req: ServerRequest): Mono<ServerResponse> =
        req.bodyToMono<User>()
            .doOnNext { userService.save(it) }
            .flatMap { ok().build() }

    open fun existsAny(req: ServerRequest): Mono<ServerResponse> = ok().body(just(userService.existsAny()))

    open fun createWithDefaults(req: ServerRequest): Mono<ServerResponse> =
        ok().body(just(userService.createWithDefaults()))

    open fun getById(req: ServerRequest): Mono<ServerResponse> =
        ok().body(just(userService.getById(req.pathVariable("userId"))))

    open fun getByEmail(req: ServerRequest): Mono<ServerResponse> =
        ok().body(just(userService.getByEmail(req.pathVariable("email"))))

    open fun existsByEmail(req: ServerRequest): Mono<ServerResponse> =
        ok().body(just(userService.existsByEmail(req.pathVariable("email"))))

    open fun existsByCompanyName(req: ServerRequest): Mono<ServerResponse> =
        ok().body(just(userService.existsByCompanyName(req.pathVariable("companyName"))))

    open fun getByProjectId(req: ServerRequest): Mono<ServerResponse> =
        ok().body(just(userService.getByProjectId(req.pathVariable("projectId"))))

    open fun getCollaborators(req: ServerRequest): Mono<ServerResponse> =
        ok().body(just(userService.getCollaborators(req.pathVariable("userId"))))

    open fun doesUserHaveProject(req: ServerRequest): Mono<ServerResponse> =
        ok().body(just(userService.doesUserHaveProject(req.pathVariable("userId"), req.pathVariable("projectId"))))

    open fun doesUserHaveCollaborator(req: ServerRequest): Mono<ServerResponse> =
        ok().body(
            just(userService.doesUserHaveCollaborator(req.pathVariable("userId"), req.pathVariable("collaboratorId")))
        )

    open fun isProjectNameUnique(req: ServerRequest): Mono<ServerResponse> =
        ok().body(
            just(
                userService.isProjectNameUnique(
                    req.pathVariable("projectName"),
                    req.pathVariable("projectId"),
                    req.pathVariable("userId")
                )
            )
        )
}
