package lt.boldadmin.nexus.backend.httpserver.handler.worklog.status.message

import lt.boldadmin.nexus.api.service.worklog.status.message.WorklogMessageService
import lt.boldadmin.nexus.api.type.valueobject.Message
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono

open class WorklogMessageHandler(private val worklogMessageService: WorklogMessageService) {

    open fun logWork(req: ServerRequest): Mono<ServerResponse> =
        req.bodyToMono<Message>()
            .doOnNext { worklogMessageService.logWork(it) }
            .flatMap { ok().build() }
}