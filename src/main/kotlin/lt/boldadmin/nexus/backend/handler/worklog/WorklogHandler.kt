package lt.boldadmin.nexus.backend.handler.worklog

import lt.boldadmin.nexus.api.service.worklog.WorklogService
import lt.boldadmin.nexus.api.type.entity.Worklog
import lt.boldadmin.nexus.api.type.valueobject.DateRange
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

    open fun getIntervalIdsByProjectIdAndDateRange(req: ServerRequest) =
        ok().body(
            Mono.just(
                worklogService.getIntervalIdsByProjectId(req.pathVariable("projectId"), createDateRange(req))
            ))

    open fun getIntervalIdsByCollaboratorIdAndDateRange(req: ServerRequest) =
        ok().body(
            Mono.just(
                worklogService.getIntervalIdsByCollaboratorId(req.pathVariable("collaboratorId"), createDateRange(req))
            ))

    open fun getIntervalEndpoints(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(worklogService.getIntervalEndpoints(req.pathVariable("intervalId"))))

    private fun createDateRange(req: ServerRequest): DateRange =
        DateRange(req.pathVariable("startDate").toLocalDate(), req.pathVariable("endDate").toLocalDate())

}
