package lt.boldadmin.nexus.backend.route

import lt.boldadmin.nexus.backend.handler.CollaboratorHandler
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RouterFunctionDsl

fun collaboratorRoutes(collaboratorHandler: CollaboratorHandler): RouterFunctionDsl.() -> Unit = {
    accept(MediaType.APPLICATION_JSON).nest {
        GET("/create-with-defaults", collaboratorHandler::createWithDefaults)
        GET("/mobile-number/{mobileNumber}", collaboratorHandler::getByMobileNumber)
        GET("/mobile-number/{mobileNumber}/exists", collaboratorHandler::existsByMobileNumber)
        POST("/work-week/validate", collaboratorHandler::validate)
        POST("/save", collaboratorHandler::save)
        GET("/{collaboratorId}", collaboratorHandler::getById)
        GET("/{collaboratorId}/coordinates", collaboratorHandler::getCoordinates)
        GET("/{collaboratorId}/exists", collaboratorHandler::existsById)
        POST("/{collaboratorId}/attribute/order-number/update", collaboratorHandler::updateOrderNumber)
        POST("/{collaboratorId}/attribute/{attributeName}/update", collaboratorHandler::update)
        POST("/{collaboratorId}/work-week/update", collaboratorHandler::updateWorkWeek)
    }
}
