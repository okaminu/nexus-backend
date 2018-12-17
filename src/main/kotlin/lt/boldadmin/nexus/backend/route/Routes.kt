package lt.boldadmin.nexus.backend.route

import lt.boldadmin.nexus.backend.handler.CompanyHandler
import lt.boldadmin.nexus.backend.handler.CountryHandler
import lt.boldadmin.nexus.backend.route.worklog.worklogRoutes
import org.springframework.beans.factory.getBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.AbstractApplicationContext
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.BodyInserters.fromObject
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.router

@Configuration
class Routes(private val applicationContext: AbstractApplicationContext) {

    @Bean
    fun router() = router {

        "/started-project-work-token".nest(startedProjectWorkTokenRoutes(applicationContext.getBean()))
        "/user".nest(userRoutes(applicationContext.getBean()))
        "/project".nest(projectRoutes(applicationContext.getBean()))
        "/customer".nest(customerRoutes(applicationContext.getBean()))
        "/collaborator".nest(collaboratorRoutes(applicationContext.getBean()))
        "/worklog".nest(worklogRoutes(applicationContext))

        GET("/is-healthy") { ok().body(fromObject(true)) }
        GET("/countries", applicationContext.getBean<CountryHandler>()::getAll)

        "/company".nest {
            accept(MediaType.APPLICATION_JSON).nest {
                GET("/name/{companyName}", applicationContext.getBean<CompanyHandler>()::getByName)
                POST("/save", applicationContext.getBean<CompanyHandler>()::save)
            }
        }

    }
}


