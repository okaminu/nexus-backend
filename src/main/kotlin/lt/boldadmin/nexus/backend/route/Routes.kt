package lt.boldadmin.nexus.backend.route

import lt.boldadmin.nexus.backend.handler.*
import lt.boldadmin.nexus.backend.handler.worklog.WorklogAuthHandler
import lt.boldadmin.nexus.backend.handler.worklog.WorklogHandler
import lt.boldadmin.nexus.backend.handler.worklog.duration.WorklogDurationHandler
import lt.boldadmin.nexus.backend.handler.worklog.status.WorklogDescriptionHandler
import lt.boldadmin.nexus.backend.handler.worklog.status.WorklogStartEndHandler
import lt.boldadmin.nexus.backend.handler.worklog.status.location.WorklogLocationHandler
import lt.boldadmin.nexus.backend.handler.worklog.status.message.WorklogMessageHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.BodyInserters.fromObject
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.router

@Configuration
class Routes(
    private val userHandler: UserHandler,
    private val projectHandler: ProjectHandler,
    private val customerHandler: CustomerHandler,
    private val countryHandler: CountryHandler,
    private val companyHandler: CompanyHandler,
    private val collaboratorHandler: CollaboratorHandler,
    private val worklogHandler: WorklogHandler,
    private val worklogAuthHandler: WorklogAuthHandler,
    private val worklogStartEndHandler: WorklogStartEndHandler,
    private val worklogDescriptionHandler: WorklogDescriptionHandler,
    private val worklogMessageHandler: WorklogMessageHandler,
    private val worklogLocationHandler: WorklogLocationHandler,
    private val worklogDurationHandler: WorklogDurationHandler,
    private val startedProjectWorkTokenHandler: StartedProjectWorkTokenHandler
) {

    @Bean
    fun router() = router {
        GET("/is-healthy") { ok().body(fromObject(true)) }

        "/started-project-work-token".nest {
            accept(MediaType.APPLICATION_JSON).nest {
                POST("/generate-and-store", startedProjectWorkTokenHandler::generateAndStore)
                POST("/delete", startedProjectWorkTokenHandler::deleteById)
                GET("/project/{projectId}/exists", startedProjectWorkTokenHandler::existsById)
                GET("/project/{projectId}/token", startedProjectWorkTokenHandler::findTokenById)
                GET("/token/{token}/id", startedProjectWorkTokenHandler::findIdByToken)
                GET("/token/{token}/project", startedProjectWorkTokenHandler::findProjectByToken)
                GET("/token/{token}/collaborators/working",
                        startedProjectWorkTokenHandler::findWorkingCollaboratorIdsByToken)
            }
        }

        "/user".nest {
            accept(MediaType.APPLICATION_JSON).nest {
                GET("/{userId}", userHandler::getById)
                GET("/{userId}/collaborator/{collaboratorId}/has-collaborator", userHandler::doesUserHaveCollaborator)
                GET("/{userId}/customer/{customerId}/has-customer", userHandler::doesUserHaveCustomer)
                GET("/{userId}/project/{projectId}/has-project", userHandler::doesUserHaveProject)
                GET("/{userId}/project/{projectId}/name/{projectName}/is-unique", userHandler::isProjectNameUnique)
                GET("/project/{projectId}", userHandler::getByProjectId)
                GET("/email/{email}", userHandler::getByEmail)
                GET("/createWithDefaults", userHandler::createWithDefaults)
                POST("/save", userHandler::save)
            }
        }

        "/project".nest {
            accept(MediaType.APPLICATION_JSON).nest {
                GET("/user/{userId}/createWithDefaults", projectHandler::createWithDefaults)
                GET("/{projectId}", projectHandler::getById)
                POST("/{projectId}/attribute/{attributeName}/update", projectHandler::update)
                POST("/{projectId}/attribute/order-number/update", projectHandler::updateOrderNumber)
            }
        }

        "/customer".nest {
            accept(MediaType.APPLICATION_JSON).nest {
                GET("/user/{userId}/createWithDefaults", customerHandler::createWithDefaults)
                GET("/{customerId}", customerHandler::getById)
                POST("/{customerId}/attribute/{attributeName}/update", customerHandler::update)
                POST("/{customerId}/attribute/order-number/update", customerHandler::updateOrderNumber)
                POST("/save", customerHandler::save)
            }
        }

        GET("/countries", countryHandler::getAll)

        "/company".nest {
            accept(MediaType.APPLICATION_JSON).nest {
                GET("/name/{companyName}", companyHandler::getByName)
                POST("/save", companyHandler::save)
            }
        }

        "/collaborator".nest {
            accept(MediaType.APPLICATION_JSON).nest {
                GET("/createWithDefaults", collaboratorHandler::createWithDefaults)
                GET("/existsById", collaboratorHandler::existsById)
                GET("/existsByMobileNumber", collaboratorHandler::existsByMobileNumber)
                GET("/getById", collaboratorHandler::getById)
                GET("/getByMobileNumber", collaboratorHandler::getByMobileNumber)
                POST("/save", collaboratorHandler::save)
                POST("/update", collaboratorHandler::update)
                POST("/updateOrderNumber", collaboratorHandler::updateOrderNumber)
            }
        }

        "/worklog".nest {
            accept(MediaType.APPLICATION_JSON).nest {
                GET("/existsByProjectIdAndCollaboratorId", worklogHandler::existsByProjectIdAndCollaboratorId)
                GET("/getByCollaboratorId", worklogHandler::getByCollaboratorId)
                GET("/getByProjectId", worklogHandler::getByProjectId)
                GET("/getIntervalEndpoints", worklogHandler::getIntervalEndpoints)
                GET("/doesCollaboratorHaveWorkLogInterval", worklogAuthHandler::doesCollaboratorHaveWorkLogInterval)
                GET("/doesCollaboratorHaveWorkLogIntervals", worklogAuthHandler::doesCollaboratorHaveWorkLogIntervals)
                GET("/doesUserHaveWorkLogInterval", worklogAuthHandler::doesUserHaveWorkLogInterval)

                "duration".nest {
                    GET("/measureDuration", worklogDurationHandler::measureDuration)
                    GET("/sumWorkDurations", worklogDurationHandler::sumWorkDurations)
                }

                "/status".nest {
                    GET("/getDescription", worklogDescriptionHandler::getDescription)
                    POST("/updateDescription", worklogDescriptionHandler::updateDescription)
                    POST("/updateDescriptionByCollaboratorId", worklogDescriptionHandler::updateDescriptionByCollaboratorId)
                    POST("/end", worklogStartEndHandler::end)
                    POST("/endWithTimestamp", worklogStartEndHandler::endWithTimestamp)
                    POST("/endAllStartedWorkWhereWorkTimeEnded", worklogStartEndHandler::endAllStartedWorkWhereWorkTimeEnded)
                    POST("/hasWorkEnded", worklogStartEndHandler::hasWorkEnded)
                    POST("/start", worklogStartEndHandler::start)
                    POST("/startWithTimestamp", worklogStartEndHandler::startWithTimestamp)
                    GET("/hasWorkStarted", worklogStartEndHandler::hasWorkStarted)
                    GET("/getProjectOfStartedWork", worklogStartEndHandler::getProjectOfStartedWork)

                    "/message".nest {
                        POST("/logWork", worklogMessageHandler::logWork)
                    }

                    "/location".nest {
                        POST("/logWork", worklogLocationHandler::logWork)
                    }
                }

            }
        }
    }

}
