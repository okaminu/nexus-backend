package lt.boldadmin.nexus.backend.exception.handler

import lt.boldadmin.nexus.api.exception.GeocodeGatewayException
import org.springframework.http.HttpStatus
import org.springframework.web.server.ServerWebExchange

class GeocodeGatewayExceptionHandler : TemplateExceptionHandler() {

    override fun canHandle(ex: Throwable) = ex is GeocodeGatewayException

    override fun handleException(exchange: ServerWebExchange, ex: Throwable) {
        exchange.response.statusCode = HttpStatus.INTERNAL_SERVER_ERROR
    }
}