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

        "started-project-work-token".nest {
            accept(MediaType.APPLICATION_JSON).nest {
                POST("/generateAndStore", startedProjectWorkTokenHandler::generateAndStore)
                POST("/delete", startedProjectWorkTokenHandler::deleteById)
                GET("/exists/project/{projectId}", startedProjectWorkTokenHandler::existsById)
                GET("/find-id/{token}", startedProjectWorkTokenHandler::findIdByToken)
                GET("/find-project/{token}", startedProjectWorkTokenHandler::findProjectByToken)
                GET("/find-token/{projectId}", startedProjectWorkTokenHandler::findTokenById)
                GET("/find-working-collaborators/token/{token}",
                        startedProjectWorkTokenHandler::findWorkingCollaboratorIdsByToken)
            }
        }

        "user".nest {
            accept(MediaType.APPLICATION_JSON).nest {
                GET("/doesUserHaveCollaborator", userHandler::doesUserHaveCollaborator)
                GET("/createWithDefaults", userHandler::createWithDefaults)
                GET("/doesUserHaveCustomer", userHandler::doesUserHaveCustomer)
                GET("/doesUserHaveProject", userHandler::doesUserHaveProject)
                POST("/save", userHandler::save)
                GET("/getById", userHandler::getById)
                GET("/getByEmail", userHandler::getByEmail)
                GET("/getByProjectId", userHandler::getByProjectId)
                GET("/isProjectNameUnique", userHandler::isProjectNameUnique)
            }
        }

        "project".nest {
            accept(MediaType.APPLICATION_JSON).nest {
                GET("/createWithDefaults", projectHandler::createWithDefaults)
                GET("/getById", projectHandler::getById)
                POST("/update", projectHandler::update)
                POST("/updateOrderNumber", projectHandler::updateOrderNumber)
            }
        }

        "customer".nest {
            accept(MediaType.APPLICATION_JSON).nest {
                GET("/createWithDefaults", customerHandler::createWithDefaults)
                GET("/getById", customerHandler::getById)
                POST("/update", customerHandler::update)
                POST("/updateOrderNumber", customerHandler::updateOrderNumber)
            }
        }

        "country".nest {
            accept(MediaType.APPLICATION_JSON).nest {
                GET("/createWithDefaults", countryHandler::getAll)
            }
        }

        "company".nest {
            accept(MediaType.APPLICATION_JSON).nest {
                GET("/getByName", companyHandler::getByName)
                POST("/save", companyHandler::save)
            }
        }

        "collaborator".nest {
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

        "worklog".nest {
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

                "status".nest {
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

                    "message".nest {
                        POST("/logWork", worklogMessageHandler::logWork)
                    }

                    "location".nest {
                        POST("/logWork", worklogLocationHandler::logWork)
                    }
                }

            }
        }
    }

}
