package lt.boldadmin.nexus.backend.route

import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.server.RouterFunctionDsl

fun collaboratorRoutes(
    collaboratorHandler: CollaboratorHandler, identityConfirmationHandler: IdentityConfirmationHandler
): RouterFunctionDsl.() -> Unit = {
    accept(APPLICATION_JSON).nest {
        GET("/workTime", collaboratorHandler::getWorkTime)
    }
    "/identity-confirmation".nest {
        "/code".nest {
            accept(APPLICATION_JSON).nest {
                POST("/request/{mobileNumber}", identityConfirmationHandler::requestCode)
                POST("/confirm/{code}", identityConfirmationHandler::confirmCode)
            }
        }
    }
}