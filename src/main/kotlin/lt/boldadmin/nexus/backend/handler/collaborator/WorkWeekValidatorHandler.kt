package lt.boldadmin.nexus.backend.handler.collaborator

import lt.boldadmin.nexus.api.service.collaborator.WorkWeekValidatorService
import lt.boldadmin.nexus.api.type.valueobject.time.DayMinuteInterval
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Mono
import java.util.*

open class WorkWeekValidatorHandler(private val workWeekValidatorService: WorkWeekValidatorService) {

    open fun validate(req: ServerRequest): Mono<ServerResponse> =
        req.bodyToMono<SortedSet<DayMinuteInterval>>()
            .flatMap { ok().body(Mono.just(workWeekValidatorService.validate(it))) }

}
