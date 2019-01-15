package lt.boldadmin.nexus.backend.route

import lt.boldadmin.nexus.backend.handler.CustomerHandler
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RouterFunctionDsl

fun customerRoutes(customerHandler: CustomerHandler): RouterFunctionDsl.() -> Unit = {
    accept(MediaType.APPLICATION_JSON).nest {
        GET("/user/{userId}/create-with-defaults", customerHandler::createWithDefaults)
        POST("/save", customerHandler::save)
        GET("/{customerId}", customerHandler::getById)
        POST("/{customerId}/attribute/{attributeName}/update", customerHandler::update)
        POST("/{customerId}/attribute/order-number/update", customerHandler::updateOrderNumber)
    }
}
