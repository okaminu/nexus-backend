package lt.boldadmin.nexus.backend.handler.worklog

import lt.boldadmin.nexus.api.service.worklog.WorklogOvertimeService
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Mono

open class WorklogOvertimeHandler(private val worklogOvertimeService: WorklogOvertimeService) {

    open fun endOnOvertime(req: ServerRequest): Mono<ServerResponse> =
        ServerResponse.ok().body(Mono.just(worklogOvertimeService.endOnOvertime()))

}
