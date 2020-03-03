package lt.boldadmin.nexus.backend.handler.worklog

import lt.boldadmin.nexus.api.service.worklog.WorklogService
import lt.boldadmin.nexus.api.type.entity.Worklog
import lt.boldadmin.nexus.api.type.valueobject.time.DateInterval
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Mono

open class WorklogHandler(private val worklogService: WorklogService) {

    open fun save(req: ServerRequest): Mono<ServerResponse> =
        req.bodyToMono<Worklog>()
            .doOnNext { worklogService.save(it) }
            .flatMap { ok().build() }

    open fun getIntervalIdsByCollaboratorId(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(worklogService.getIntervalIdsByCollaboratorId(req.pathVariable("collaboratorId"))))

    open fun getIntervalIdsByProjectId(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(worklogService.getIntervalIdsByProjectId(req.pathVariable("projectId"))))

    open fun getIntervalIdsByProjectIdAndDateInterval(req: ServerRequest) =
        ok().body(
            Mono.just(
                worklogService.getIntervalIdsByProjectId(req.pathVariable("projectId"), createInterval(req))
            ))

    open fun getIntervalIdsByCollaboratorIdAndDateInterval(req: ServerRequest) =
        ok().body(
            Mono.just(
                worklogService.getIntervalIdsByCollaboratorId(req.pathVariable("collaboratorId"), createInterval(req))
            ))

    open fun getIntervalEndpoints(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(worklogService.getIntervalEndpoints(req.pathVariable("intervalId"))))

    private fun createInterval(req: ServerRequest): DateInterval =
        DateInterval(req.pathVariable("startDate").toLocalDate(), req.pathVariable("endDate").toLocalDate())

}
