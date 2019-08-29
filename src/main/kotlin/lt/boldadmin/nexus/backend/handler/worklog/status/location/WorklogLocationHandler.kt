package lt.boldadmin.nexus.backend.handler.worklog.status.location

import lt.boldadmin.nexus.api.service.worklog.status.location.WorklogLocationService
import lt.boldadmin.nexus.api.type.valueobject.Coordinates
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono

open class WorklogLocationHandler(private val worklogLocationService: WorklogLocationService) {

    open fun logWork(req: ServerRequest): Mono<ServerResponse> =
        req.bodyToMono<Pair<String, Coordinates>>()
            .doOnNext { worklogLocationService.logWork(it.first, it.second) }
            .flatMap { ok().build() }
}