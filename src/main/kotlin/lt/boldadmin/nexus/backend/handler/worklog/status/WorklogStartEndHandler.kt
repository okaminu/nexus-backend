package lt.boldadmin.nexus.backend.handler.worklog.status

import lt.boldadmin.nexus.api.service.worklog.status.WorklogStartEndService
import lt.boldadmin.nexus.api.type.entity.Collaborator
import lt.boldadmin.nexus.api.type.entity.Project
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono

open class WorklogStartEndHandler(private val worklogStartEndService: WorklogStartEndService) {

    open fun getProjectOfStartedWork(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(worklogStartEndService.getProjectOfStartedWork(req.pathVariable("collaboratorId"))))

    open fun start(req: ServerRequest): Mono<ServerResponse> =
        req.bodyToMono<Pair<Collaborator, Project>>()
            .doOnNext { worklogStartEndService.start(it.first, it.second) }
            .flatMap { ok().build() }

    open fun startWithTimestamp(req: ServerRequest): Mono<ServerResponse> =
        req.bodyToMono<Pair<Collaborator, Project>>()
            .doOnNext { worklogStartEndService.start(it.first, it.second, req.pathVariable("timestamp").toLong()) }
            .flatMap { ok().build() }

    open fun hasWorkStarted(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(worklogStartEndService.hasWorkStarted(req.pathVariable("collaboratorId"))))

    open fun end(req: ServerRequest): Mono<ServerResponse> =
        req.bodyToMono<Collaborator>()
            .doOnNext {
                worklogStartEndService.end(it) }
            .flatMap { ok().build() }

    open fun endWithTimestamp(req: ServerRequest): Mono<ServerResponse> =
        req.bodyToMono<Collaborator>()
            .doOnNext { worklogStartEndService.end(it, req.pathVariable("timestamp").toLong()) }
            .flatMap { ok().build() }

    open fun endAllStartedWorkWhereWorkTimeEnded(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(worklogStartEndService.endAllStartedWorkWhereWorkTimeEnded()))

    open fun hasWorkEnded(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(worklogStartEndService.hasWorkEnded(req.pathVariable("collaboratorId"))))

}