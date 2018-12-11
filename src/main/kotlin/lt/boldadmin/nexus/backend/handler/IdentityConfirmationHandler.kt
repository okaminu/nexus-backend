package lt.boldadmin.nexus.backend.handler

import lt.boldadmin.crowbar.IdentityConfirmation
import lt.boldadmin.crowbar.type.entity.UserConfirmationCode
import lt.boldadmin.nexus.api.service.CollaboratorService
import org.springframework.web.reactive.function.BodyInserters.fromObject
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Mono

open class IdentityConfirmationHandler(
    private val identityConfirmation: IdentityConfirmation,
    private val collaboratorService: CollaboratorService
) {

    open fun requestCode(req: ServerRequest): Mono<ServerResponse> =
        Mono.just(req.pathVariable("mobileNumber"))
            .doOnNext { identityConfirmation.sendConfirmationCode(getCollaborator(it).id!!, it) }
            .flatMap { ok().build() }


    open fun confirmCode(req: ServerRequest): Mono<ServerResponse> =
        Mono.just(req.pathVariable("code"))
            .map { UserConfirmationCode(identityConfirmation.getUserIdByCode(it), it) }
            .doOnNext { identityConfirmation.confirmCode(it.code) }
            .flatMap { ok().body(fromObject(identityConfirmation.getTokenById(it.id))) }

    private fun getCollaborator(mobileNumber: String) = collaboratorService.getByMobileNumber(mobileNumber)

}