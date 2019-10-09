package lt.boldadmin.nexus.backend.handler.worklog

import lt.boldadmin.nexus.api.service.worklog.status.WorklogOvertimeService
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono

open class WorklogOvertimeHandler(private val worklogOvertimeService: WorklogOvertimeService) {

    open fun endOnOvertime(req: ServerRequest): Mono<ServerResponse> =
        ServerResponse.ok().body(Mono.just(worklogOvertimeService.endOnOvertime()))

}
