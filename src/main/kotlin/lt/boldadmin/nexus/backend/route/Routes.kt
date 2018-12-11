package lt.boldadmin.nexus.backend.route

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.BodyInserters.fromObject
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.router

@Configuration
class Routes(
    private val workLogHandler: WorkLogHandler,
    private val collaboratorHandler: CollaboratorHandler,
    private val identityConfirmationHandler: IdentityConfirmationHandler,
    private val workLogMessageHandler: WorkLogMessageHandler
) {

    @Bean
    fun router() = router {
        "/collaborator".nest(collaboratorRoutes(collaboratorHandler, identityConfirmationHandler))
        "/worklog".nest(workLogRoutes(workLogHandler, workLogMessageHandler))
        GET("/is-healthy") { ok().body(fromObject(true)) }
    }

}
