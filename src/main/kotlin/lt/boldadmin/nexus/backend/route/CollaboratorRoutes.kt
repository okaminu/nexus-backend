package lt.boldadmin.nexus.backend.route

import lt.boldadmin.nexus.backend.handler.collaborator.CollaboratorCoordinatesHandler
import lt.boldadmin.nexus.backend.handler.collaborator.CollaboratorHandler
import lt.boldadmin.nexus.backend.handler.collaborator.WorkWeekValidatorHandler
import org.springframework.beans.factory.getBean
import org.springframework.context.support.AbstractApplicationContext
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RouterFunctionDsl

fun collaboratorRoutes(applicationContext: AbstractApplicationContext): RouterFunctionDsl.() -> Unit = {

    val collaboratorHandler: CollaboratorHandler = applicationContext.getBean()
    val workWeekValidatorHandler: WorkWeekValidatorHandler = applicationContext.getBean()
    val collaboratorCoordinatesHandler: CollaboratorCoordinatesHandler = applicationContext.getBean()

    accept(MediaType.APPLICATION_JSON).nest {
        GET("/create-with-defaults", collaboratorHandler::createWithDefaults)
        GET("/mobile-number/{mobileNumber}", collaboratorHandler::getByMobileNumber)
        GET("/mobile-number/{mobileNumber}/exists", collaboratorHandler::existsByMobileNumber)
        POST("/work-week/validate", workWeekValidatorHandler::validate)
        POST("/save", collaboratorHandler::save)
        GET("/{collaboratorId}", collaboratorHandler::getById)
        GET("/{collaboratorId}/coordinates", collaboratorCoordinatesHandler::getCoordinates)
        GET("/{collaboratorId}/exists", collaboratorHandler::existsById)
        POST("/{collaboratorId}/attribute/order-number/update", collaboratorHandler::updateOrderNumber)
        POST("/{collaboratorId}/attribute/{attributeName}/update", collaboratorHandler::update)
        POST("/{collaboratorId}/work-week/update", collaboratorHandler::updateWorkWeek)
    }
}
