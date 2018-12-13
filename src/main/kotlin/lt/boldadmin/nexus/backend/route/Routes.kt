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
                "/token/{token}".nest {
                    GET("/id", startedProjectWorkTokenHandler::findIdByToken)
                    GET("/project", startedProjectWorkTokenHandler::findProjectByToken)
                    GET("/collaborators/working",
                            startedProjectWorkTokenHandler::findWorkingCollaboratorIdsByToken)
                }
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
                GET("/create-with-defaults", userHandler::createWithDefaults)
                POST("/save", userHandler::save)
            }
        }

        "/project".nest {
            accept(MediaType.APPLICATION_JSON).nest {
                GET("/user/{userId}/create-with-defaults", projectHandler::createWithDefaults)
                GET("/{projectId}", projectHandler::getById)
                POST("/{projectId}/attribute/{attributeName}/update", projectHandler::update)
                POST("/{projectId}/attribute/order-number/update", projectHandler::updateOrderNumber)
            }
        }

        "/customer".nest {
            accept(MediaType.APPLICATION_JSON).nest {
                GET("/user/{userId}/createWithDefaults", customerHandler::createWithDefaults)
                POST("/save", customerHandler::save)
                GET("/{customerId}", customerHandler::getById)
                POST("/{customerId}/attribute/{attributeName}/update", customerHandler::update)
                POST("/{customerId}/attribute/order-number/update", customerHandler::updateOrderNumber)
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
                GET("/create-with-defaults", collaboratorHandler::createWithDefaults)
                GET("/mobile-number/{mobileNumber}/exists", collaboratorHandler::existsByMobileNumber)
                GET("/mobile-number/{mobileNumber}", collaboratorHandler::getByMobileNumber)
                POST("/save", collaboratorHandler::save)
                GET("/{collaboratorId}", collaboratorHandler::getById)
                GET("/{collaboratorId}/exists", collaboratorHandler::existsById)
                POST("/{collaboratorId}/attribute/{attributeName}/update", collaboratorHandler::update)
                POST("/{collaboratorId}/attribute/order-number/update", collaboratorHandler::updateOrderNumber)
            }
        }

        "/worklog".nest {
            accept(MediaType.APPLICATION_JSON).nest {
                GET("/intervals/{intervalIds}/durations-sum", worklogDurationHandler::sumWorkDurations)
                GET("/intervals/{intervalIds}/collaborator/{collaboratorId}/has-intervals",
                        worklogAuthHandler::doesCollaboratorHaveWorkLogIntervals)
                GET("/project/{projectId}/collaborator/{collaboratorId}/exists", worklogHandler::existsByProjectIdAndCollaboratorId)
                GET("/project/{projectId}", worklogHandler::getByProjectId)
                GET("/interval/{intervalId}/status/description", worklogDescriptionHandler::getDescription)
                GET("/interval/{intervalId}", worklogHandler::getIntervalEndpoints)
                POST("/interval/{intervalId}/status/description/update", worklogDescriptionHandler::updateDescription)
                GET("/duration", worklogDurationHandler::measureDuration)
                GET("/user/{userId}/has-interval", worklogAuthHandler::doesUserHaveWorkLogInterval)
                GET("/collaborator/{collaboratorId}/has-interval", worklogAuthHandler::doesCollaboratorHaveWorkLogInterval)
                }

                "/collaborator/{collaboratorId}".nest {
                    GET("", worklogHandler::getByCollaboratorId)
                    "/status".nest {
                        GET("/has-work-started", worklogStartEndHandler::hasWorkStarted)
                        GET("/has-work-ended", worklogStartEndHandler::hasWorkEnded)
                        GET("/project-of-started-work", worklogStartEndHandler::getProjectOfStartedWork)
                        POST("/description/update", worklogDescriptionHandler::updateDescriptionByCollaboratorId)
                    }
                }

                "/status".nest {
                    POST("/end", worklogStartEndHandler::end)
                    POST("/end/timestamp/{timestamp}", worklogStartEndHandler::endWithTimestamp)
                    POST("/end/all-started-work-where-worktime-ended",
                            worklogStartEndHandler::endAllStartedWorkWhereWorkTimeEnded)
                    POST("/start", worklogStartEndHandler::start)
                    POST("/start/timestamp/{timestamp}", worklogStartEndHandler::startWithTimestamp)

                    "/log-work".nest {
                        POST("/message", worklogMessageHandler::logWork)
                        POST("/location", worklogLocationHandler::logWork)
                    }
                }

            }
        }
    }


