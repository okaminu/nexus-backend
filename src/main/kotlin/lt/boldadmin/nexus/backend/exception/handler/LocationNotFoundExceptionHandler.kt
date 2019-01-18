package lt.boldadmin.nexus.backend.exception.handler

import lt.boldadmin.nexus.api.exception.LocationNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.server.ServerWebExchange

class LocationNotFoundExceptionHandler : TemplateExceptionHandler() {

    override fun canHandle(ex: Throwable) = ex is LocationNotFoundException

    override fun handleException(exchange: ServerWebExchange, ex: Throwable) {
        exchange.response.statusCode = HttpStatus.UNPROCESSABLE_ENTITY
    }
}