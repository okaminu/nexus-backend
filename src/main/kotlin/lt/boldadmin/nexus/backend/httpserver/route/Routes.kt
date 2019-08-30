package lt.boldadmin.nexus.backend.httpserver.route

import lt.boldadmin.nexus.backend.httpserver.handler.CountryHandler
import lt.boldadmin.nexus.backend.httpserver.route.worklog.worklogRoutes
import org.springframework.beans.factory.getBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.AbstractApplicationContext
import org.springframework.web.reactive.function.BodyInserters.fromObject
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.router

@Configuration
class Routes(private val applicationContext: AbstractApplicationContext) {

    @Bean
    fun router() = router {
        "/user".nest(userRoutes(applicationContext.getBean()))
        "/project".nest(projectRoutes(applicationContext.getBean()))
        "/collaborator".nest(collaboratorRoutes(applicationContext.getBean()))
        "/worklog".nest(worklogRoutes(applicationContext))

        GET("/is-healthy") { ok().body(fromObject(true)) }
        GET("/countries", applicationContext.getBean<CountryHandler>()::getAll)
    }
}


