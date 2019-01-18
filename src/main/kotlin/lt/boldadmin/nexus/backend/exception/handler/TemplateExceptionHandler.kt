package lt.boldadmin.nexus.backend.exception.handler

import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebExceptionHandler
import reactor.core.publisher.Mono

abstract class TemplateExceptionHandler : WebExceptionHandler {

    final override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> {
        ex.printStackTrace()

        if (canHandle(ex)) {
            handleException(exchange, ex)
            return Mono.empty()
        }
        return Mono.error(ex)
    }

    abstract fun canHandle(ex: Throwable): Boolean

    abstract fun handleException(exchange: ServerWebExchange, ex: Throwable)

}