package lt.boldadmin.nexus.backend.handler

import lt.boldadmin.nexus.api.service.CustomerService
import lt.boldadmin.nexus.api.type.entity.Customer
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Mono

open class CustomerHandler(private val customerService: CustomerService) {

    open fun createWithDefaults(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(customerService.createWithDefaults(req.pathVariable("userId"))))

    open fun getById(req: ServerRequest): Mono<ServerResponse> =
        ok().body(Mono.just(customerService.getById(req.pathVariable("customerId"))))

    open fun save(req: ServerRequest): Mono<ServerResponse> =
        req.bodyToMono<Customer>()
            .doOnNext { customerService.save(it) }
            .flatMap { ok().build() }

    open fun update(req: ServerRequest): Mono<ServerResponse> =
        req.bodyToMono<String>()
            .defaultIfEmpty("")
            .doOnNext { customerService.update(req.pathVariable("customerId"), req.pathVariable("attributeName"), it) }
            .flatMap { ok().build() }

    open fun updateOrderNumber(req: ServerRequest): Mono<ServerResponse> =
        req.bodyToMono<Short>()
            .doOnNext { customerService.updateOrderNumber(req.pathVariable("customerId"), it) }
            .flatMap { ok().build() }

}