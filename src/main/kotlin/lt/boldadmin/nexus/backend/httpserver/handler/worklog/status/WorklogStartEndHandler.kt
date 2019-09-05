package lt.boldadmin.nexus.backend.httpserver.handler.worklog.status

import lt.boldadmin.nexus.api.service.worklog.status.WorklogStartEndService
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Mono

open class WorklogStartEndHandler(private val worklogStartEndService: WorklogStartEndService) {

    open fun getProjectOfStartedWork(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(worklogStartEndService.getProjectOfStartedWork(req.pathVariable("collaboratorId"))))

    open fun hasWorkStarted(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(worklogStartEndService.hasWorkStarted(req.pathVariable("collaboratorId"))))

    open fun hasWorkStartedInProject(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(worklogStartEndService.hasWorkStarted(req.pathVariable("collaboratorId"),
            req.pathVariable("projectId"))))

    open fun endAllStartedWorkWhereWorkTimeEnded(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(worklogStartEndService.endAllStartedWorkWhereWorkTimeEnded()))
}