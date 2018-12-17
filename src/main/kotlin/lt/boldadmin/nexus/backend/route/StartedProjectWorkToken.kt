package lt.boldadmin.nexus.backend.route

import lt.boldadmin.nexus.backend.handler.StartedProjectWorkTokenHandler
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RouterFunctionDsl

fun startedProjectWorkTokenRoutes(startedProjectWorkTokenHandler: StartedProjectWorkTokenHandler)
        : RouterFunctionDsl.() -> Unit = {
    accept(MediaType.APPLICATION_JSON).nest {
        POST("/generate-and-store", startedProjectWorkTokenHandler::generateAndStore)
        POST("/delete", startedProjectWorkTokenHandler::deleteById)
        GET("/project/{projectId}/exists", startedProjectWorkTokenHandler::existsById)
        GET("/project/{projectId}/token", startedProjectWorkTokenHandler::findTokenById)
        "/token/{token}".nest {
            GET("/id", startedProjectWorkTokenHandler::findIdByToken)
            GET("/project", startedProjectWorkTokenHandler::findProjectByToken)
            GET("/collaborators/working", startedProjectWorkTokenHandler::findWorkingCollaboratorIdsByToken
            )
        }
    }
}
