package lt.boldadmin.nexus.backend.handler.worklog

import lt.boldadmin.nexus.api.service.worklog.WorklogService
import lt.boldadmin.nexus.api.type.entity.Worklog
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono

open class WorklogHandler(private val worklogService: WorklogService) {

    open fun save(req: ServerRequest): Mono<ServerResponse> =
        req.bodyToMono<Worklog>()
            .doOnNext { worklogService.save(it) }
            .flatMap { ok().build() }

    open fun getByCollaboratorId(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(worklogService.getByCollaboratorId(req.pathVariable("collaboratorId"))))

    open fun getByProjectId(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(worklogService.getByProjectId(req.pathVariable("projectId"))))

    open fun getIntervalEndpoints(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(worklogService.getIntervalEndpoints(req.pathVariable("intervalId"))))

}