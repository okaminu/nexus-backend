package lt.boldadmin.nexus.backend.handler

import lt.boldadmin.nexus.api.service.CountryService
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Mono

open class CountryHandler(private val countryService: CountryService) {

    open fun getAll(req: ServerRequest): Mono<ServerResponse> = ok().body(Mono.just(countryService.countries))
}
