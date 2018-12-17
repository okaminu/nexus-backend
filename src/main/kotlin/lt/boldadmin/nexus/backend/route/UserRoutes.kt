package lt.boldadmin.nexus.backend.route

import lt.boldadmin.nexus.backend.handler.UserHandler
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RouterFunctionDsl

fun userRoutes(userHandler: UserHandler): RouterFunctionDsl.() -> Unit = {
    accept(MediaType.APPLICATION_JSON).nest {
        GET("/{userId}", userHandler::getById)
        GET("/{userId}/collaborator/{collaboratorId}/has-collaborator", userHandler::doesUserHaveCollaborator)
        GET("/{userId}/customer/{customerId}/has-customer", userHandler::doesUserHaveCustomer)
        GET("/{userId}/project/{projectId}/has-project", userHandler::doesUserHaveProject)
        GET("/{userId}/project/{projectId}/name/{projectName}/is-unique", userHandler::isProjectNameUnique)
        GET("/project/{projectId}", userHandler::getByProjectId)
        GET("/email/{email}", userHandler::getByEmail)
        GET("/create-with-defaults", userHandler::createWithDefaults)
        POST("/save", userHandler::save)
    }
}
