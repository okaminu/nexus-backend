package lt.boldadmin.nexus.backend.route.worklog

import lt.boldadmin.nexus.backend.handler.worklog.WorklogAuthHandler
import lt.boldadmin.nexus.backend.handler.worklog.WorklogHandler
import lt.boldadmin.nexus.backend.handler.worklog.duration.WorklogDurationHandler
import lt.boldadmin.nexus.backend.handler.worklog.status.WorklogDescriptionHandler
import lt.boldadmin.nexus.backend.handler.worklog.status.WorklogStartEndHandler
import lt.boldadmin.nexus.backend.handler.worklog.status.location.WorklogLocationHandler
import lt.boldadmin.nexus.backend.handler.worklog.status.message.WorklogMessageHandler
import org.springframework.beans.factory.getBean
import org.springframework.context.support.AbstractApplicationContext
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RouterFunctionDsl

fun worklogRoutes(applicationContext: AbstractApplicationContext): RouterFunctionDsl.() -> Unit = {

    val worklogAuthHandler: WorklogAuthHandler = applicationContext.getBean()
    val worklogDescriptionHandler: WorklogDescriptionHandler = applicationContext.getBean()
    val worklogDurationHandler: WorklogDurationHandler = applicationContext.getBean()
    val worklogHandler: WorklogHandler = applicationContext.getBean()
    val worklogLocationHandler: WorklogLocationHandler = applicationContext.getBean()
    val worklogMessageHandler: WorklogMessageHandler = applicationContext.getBean()
    val worklogStartEndHandler: WorklogStartEndHandler = applicationContext.getBean()


    accept(MediaType.APPLICATION_JSON).nest {
        POST("/save", worklogHandler::save)
        "/status".nest(worklogStatusRoutes(worklogStartEndHandler, worklogMessageHandler, worklogLocationHandler))
        "/collaborator".nest{
            GET("/{collaboratorId}", worklogHandler::getByCollaboratorId)
            GET("/{collaboratorId}/status/has-work-started", worklogStartEndHandler::hasWorkStarted)
            GET("/{collaboratorId}/status/has-work-ended", worklogStartEndHandler::hasWorkEnded)
            GET("/{collaboratorId}/status/project-of-started-work", worklogStartEndHandler::getProjectOfStartedWork)
            POST("/{collaboratorId}/status/description/update",
                worklogDescriptionHandler::updateDescriptionByCollaboratorId)
        }

        GET("/project/{projectId}/collaborator/{collaboratorId}/exists",
            worklogHandler::existsByProjectIdAndCollaboratorId)
        GET("/project/{projectId}", worklogHandler::getByProjectId)

        "/interval".nest {
            GET("/{intervalId}/endpoints", worklogHandler::getIntervalEndpoints)
            GET("/{intervalId}/status/description", worklogDescriptionHandler::getDescription)
            GET("/{intervalId}/user/{userId}/has-interval", worklogAuthHandler::doesUserHaveWorkLogInterval)
            GET("/{intervalId}/collaborator/{collaboratorId}/has-interval",
                    worklogAuthHandler::doesCollaboratorHaveWorkLogInterval)
            GET("/{intervalId}/duration", worklogDurationHandler::measureDuration)
            POST("/{intervalId}/status/description/update", worklogDescriptionHandler::updateDescription)
        }
        GET("/intervals/{intervalIds}/durations-sum", worklogDurationHandler::sumWorkDurations)
        GET("/intervals/{intervalIds}/collaborator/{collaboratorId}/has-intervals",
            worklogAuthHandler::doesCollaboratorHaveWorkLogIntervals)
    }

}
