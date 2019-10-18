package lt.boldadmin.nexus.backend.handler.worklog.duration

import lt.boldadmin.nexus.api.service.worklog.duration.WorklogDurationService
import lt.boldadmin.nexus.api.type.valueobject.DateRange
import lt.boldadmin.nexus.backend.handler.worklog.toLocalDate
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Mono

open class WorklogDurationHandler(private val worklogDurationService: WorklogDurationService) {

    open fun measureDuration(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(worklogDurationService.measureDuration(req.pathVariable("intervalId"))))

    open fun sumWorkDurationsByCollaboratorId(req: ServerRequest): Mono<ServerResponse> =
        ok().body(
            Mono.just(
                worklogDurationService.sumWorkDurationsByCollaboratorId(req.pathVariable("collaboratorId"))
            )
        )

    open fun sumWorkDurationsByCollaboratorIdAndDateRange(req: ServerRequest): Mono<ServerResponse> =
        ok().body(
            Mono.just(
                worklogDurationService.sumWorkDurationsByCollaboratorId(
                    req.pathVariable("collaboratorId"),
                    createDateRange(req)
                )
            )
        )

    open fun sumWorkDurationsByProjectIdAndDateRange(req: ServerRequest): Mono<ServerResponse> =
        ok().body(
            Mono.just(
                worklogDurationService.sumWorkDurationsByProjectId(
                    req.pathVariable("projectId"),
                    createDateRange(req)
                )
            )
        )

    open fun sumWorkDurationsByProjectId(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(worklogDurationService.sumWorkDurationsByProjectId(req.pathVariable("projectId"))))

    private fun createDateRange(req: ServerRequest): DateRange =
        DateRange(req.pathVariable("startDate").toLocalDate(), req.pathVariable("endDate").toLocalDate())

}
