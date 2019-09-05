package lt.boldadmin.nexus.backend.route

import lt.boldadmin.nexus.backend.handler.ProjectHandler
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RouterFunctionDsl

fun projectRoutes(projectHandler: ProjectHandler): RouterFunctionDsl.() -> Unit = {
    accept(MediaType.APPLICATION_JSON).nest {
        GET("/user/{userId}/create-with-defaults", projectHandler::createWithDefaults)
        GET("/{projectId}", projectHandler::getById)
        POST("/{projectId}/attribute/order-number/update", projectHandler::updateOrderNumber)
        POST("/{projectId}/attribute/location/update", projectHandler::updateLocation)
        POST("/{projectId}/attribute/{attributeName}/update", projectHandler::update)
        DELETE("/{projectId}/attribute/location", projectHandler::deleteLocation)
    }
}
