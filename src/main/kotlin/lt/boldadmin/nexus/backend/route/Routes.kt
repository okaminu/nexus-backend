package lt.boldadmin.nexus.backend.route

import lt.boldadmin.nexus.backend.handler.CountryHandler
import org.springframework.beans.factory.getBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.AbstractApplicationContext
import org.springframework.web.reactive.function.BodyInserters.fromObject
import org.springframework.web.reactive.function.server.router

@Configuration
class Routes(private val applicationContext: AbstractApplicationContext) {

    @Bean
    fun router() = router {
        "/user".nest(userRoutes(applicationContext.getBean()))
        "/project".nest(projectRoutes(applicationContext.getBean()))
        "/worklog".nest(worklogRoutes(applicationContext))
        "/collaborator".nest(
            collaboratorRoutes(
                applicationContext.getBean(),
                applicationContext.getBean(),
                applicationContext.getBean())
        )

        GET("/is-healthy") { ok().body(fromObject(true)) }
        GET("/countries", applicationContext.getBean<CountryHandler>()::getAll)
    }
}
