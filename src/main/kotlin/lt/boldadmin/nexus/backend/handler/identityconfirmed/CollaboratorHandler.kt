package lt.boldadmin.nexus.backend.handler.identityconfirmed

import org.springframework.web.reactive.function.BodyInserters.fromObject
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Mono

open class CollaboratorHandler(private val collaboratorAuthService: CollaboratorAuthenticationService) {

    open fun getWorkTime(req: ServerRequest): Mono<ServerResponse> =
        ok().body(fromObject(collaboratorAuthService.getCollaborator(req).workTime))

}