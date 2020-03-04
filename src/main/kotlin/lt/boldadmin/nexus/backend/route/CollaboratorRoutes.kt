package lt.boldadmin.nexus.backend.route

import lt.boldadmin.nexus.backend.handler.collaborator.CollaboratorCoordinatesHandler
import lt.boldadmin.nexus.backend.handler.collaborator.CollaboratorHandler
import lt.boldadmin.nexus.backend.handler.collaborator.WorkWeekValidatorHandler
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RouterFunctionDsl

fun collaboratorRoutes(
    collaboratorHandler: CollaboratorHandler,
    workWeekValidatorHandler: WorkWeekValidatorHandler,
    coordinatesHandler: CollaboratorCoordinatesHandler
): RouterFunctionDsl.() -> Unit = {

    accept(MediaType.APPLICATION_JSON).nest {
        GET("/create-with-defaults", collaboratorHandler::createWithDefaults)
        GET("/mobile-number/{mobileNumber}", collaboratorHandler::getByMobileNumber)
        GET("/mobile-number/{mobileNumber}/exists", collaboratorHandler::existsByMobileNumber)
        POST("/work-week/validate", workWeekValidatorHandler::validate)
        POST("/save", collaboratorHandler::save)
        GET("/{collaboratorId}", collaboratorHandler::getById)
        GET("/{collaboratorId}/coordinates", coordinatesHandler::getCoordinates)
        GET("/{collaboratorId}/exists", collaboratorHandler::existsById)
        POST("/{collaboratorId}/attribute/order-number/update", collaboratorHandler::updateOrderNumber)
        POST("/{collaboratorId}/attribute/{attributeName}/update", collaboratorHandler::update)
        POST("/{collaboratorId}/work-week/update", collaboratorHandler::updateWorkWeek)
    }
}
