package lt.boldadmin.nexus.backend.handler.worklog.duration

import lt.boldadmin.nexus.api.service.worklog.duration.WorklogDurationService
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Mono

open class WorklogDurationHandler(private val worklogDurationService: WorklogDurationService) {

    open fun measureDuration(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(worklogDurationService.measureDuration(req.pathVariable("intervalId"))))

    open fun sumWorkDurations(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(worklogDurationService.sumWorkDurations(req.pathVariable("intervalIds").split(","))))

    open fun getWorkdurationSumByCollaboratorId(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(worklogDurationService.sumWorkDurationsByCollaboratorId(req.pathVariable("collaboratorId"))))

    open fun getWorkdurationSumByProjectId(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(worklogDurationService.sumWorkDurationsByProjectId(req.pathVariable("projectId"))))
}