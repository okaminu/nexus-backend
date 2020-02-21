package lt.boldadmin.nexus.backend.handler.collaborator

import lt.boldadmin.nexus.api.service.collaborator.CollaboratorCoordinatesService
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Mono

open class CollaboratorCoordinatesHandler(private val collaboratorCoordinatesService: CollaboratorCoordinatesService) {

    open fun getCoordinates(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(collaboratorCoordinatesService.getByCollaboratorId(req.pathVariable("collaboratorId"))))

}
