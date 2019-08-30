package lt.boldadmin.nexus.backend.httpserver.route

import lt.boldadmin.nexus.backend.httpserver.handler.UserHandler
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RouterFunctionDsl

fun userRoutes(userHandler: UserHandler): RouterFunctionDsl.() -> Unit = {
    accept(MediaType.APPLICATION_JSON).nest {
        GET("/create-with-defaults", userHandler::createWithDefaults)
        POST("/save", userHandler::save)
        GET("/exists-any", userHandler::existsAny)
        GET("/{userId}", userHandler::getById)
        GET("/{userId}/collaborator/{collaboratorId}/has-collaborator", userHandler::doesUserHaveCollaborator)
        GET("/{userId}/project/{projectId}/has-project", userHandler::doesUserHaveProject)
        GET("/{userId}/project/{projectId}/name/{projectName}/is-unique", userHandler::isProjectNameUnique)
        GET("/project/{projectId}", userHandler::getByProjectId)
        GET("/{userId}/collaborators", userHandler::getCollaborators)
        GET("/email/{email}", userHandler::getByEmail)
        GET("/email/{email}/exists", userHandler::existsByEmail)
        GET("/company-name/{companyName}/exists", userHandler::existsByCompanyName)
    }
}
