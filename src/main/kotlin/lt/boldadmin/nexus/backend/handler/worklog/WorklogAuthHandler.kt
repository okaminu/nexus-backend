package lt.boldadmin.nexus.backend.handler.worklog

import lt.boldadmin.nexus.api.service.worklog.WorklogAuthService
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Mono

open class WorklogAuthHandler(private val worklogAuthService: WorklogAuthService) {

    open fun doesUserHaveWorkLogInterval(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(
                worklogAuthService.doesUserHaveWorkLogInterval(
                        req.pathVariable("userId"), req.pathVariable("intervalId"))
            )
        )

    open fun doesCollaboratorHaveWorkLogInterval(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(
                worklogAuthService.doesCollaboratorHaveWorkLogInterval(
                        req.pathVariable("collaboratorId"),
                        req.pathVariable("intervalId")
                )
        ))

    open fun doesCollaboratorHaveWorkLogIntervals(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(
                worklogAuthService.doesCollaboratorHaveWorkLogIntervals(
                        req.pathVariable("collaboratorId"),
                        req.pathVariable("intervalIds").split(",")
                )
        ))
}
