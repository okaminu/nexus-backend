package lt.boldadmin.nexus.backend.handler.worklog

import lt.boldadmin.nexus.api.service.worklog.WorklogStatusService
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Mono

open class WorklogStatusHandler(private val worklogStatusService: WorklogStatusService) {

    open fun getProjectOfStartedWork(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(worklogStatusService.getProjectOfStartedWork(req.pathVariable("collaboratorId"))))

    open fun hasWorkStarted(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(worklogStatusService.hasWorkStarted(req.pathVariable("collaboratorId"))))

    open fun hasWorkStartedInProject(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(worklogStatusService.hasWorkStarted(req.pathVariable("collaboratorId"),
            req.pathVariable("projectId"))))
}