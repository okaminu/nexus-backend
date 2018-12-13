package lt.boldadmin.nexus.backend.handler

import lt.boldadmin.nexus.api.service.CompanyService
import lt.boldadmin.nexus.api.type.entity.Company
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono

open class CompanyHandler(private val companyService: CompanyService) {

    open fun save(req: ServerRequest): Mono<ServerResponse> =
        req.bodyToMono<Company>()
            .doOnNext { companyService.save(it) }
            .flatMap { ok().build() }

    open fun getByName(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(companyService.getByName(req.pathVariable("companyName"))?: ""))

}