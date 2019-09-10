package lt.boldadmin.nexus.backend.route.worklog

import lt.boldadmin.nexus.backend.handler.worklog.WorklogAuthHandler
import lt.boldadmin.nexus.backend.handler.worklog.WorklogHandler
import lt.boldadmin.nexus.backend.handler.worklog.duration.WorklogDurationHandler
import lt.boldadmin.nexus.backend.handler.worklog.status.WorklogStartEndHandler
import org.springframework.beans.factory.getBean
import org.springframework.context.support.AbstractApplicationContext
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RouterFunctionDsl

fun worklogRoutes(applicationContext: AbstractApplicationContext): RouterFunctionDsl.() -> Unit = {

    val worklogAuthHandler: WorklogAuthHandler = applicationContext.getBean()
    val worklogDurationHandler: WorklogDurationHandler = applicationContext.getBean()
    val worklogHandler: WorklogHandler = applicationContext.getBean()
    val worklogStartEndHandler: WorklogStartEndHandler = applicationContext.getBean()

    accept(MediaType.APPLICATION_JSON).nest {
        POST("/save", worklogHandler::save)
        POST(
            "/status/end/all-started-work-on-ended-work-time",
            worklogStartEndHandler::endAllStartedWorkWhereWorkTimeEnded
        )

        "/collaborator".nest {
            GET("/{collaboratorId}/interval-ids", worklogHandler::getByCollaboratorId)
            GET("/{collaboratorId}/status/has-work-started", worklogStartEndHandler::hasWorkStarted)
            GET("/{collaboratorId}/project/{projectId}/status/has-work-started",
                worklogStartEndHandler::hasWorkStartedInProject
            )
            GET("/{collaboratorId}/status/project-of-started-work", worklogStartEndHandler::getProjectOfStartedWork)
            GET("/{collaboratorId}/durations-sum", worklogDurationHandler::sumWorkDurationsByCollaboratorId)
        }
        "/project".nest {
            GET("/{projectId}/interval-ids", worklogHandler::getByProjectId)
            GET("/{projectId}/durations-sum", worklogDurationHandler::sumWorkDurationsByProjectId)
        }

        "/interval".nest {
            GET("/{intervalId}/endpoints", worklogHandler::getIntervalEndpoints)
            GET("/{intervalId}/user/{userId}/has-interval", worklogAuthHandler::doesUserHaveWorkLogInterval)
            GET("/{intervalId}/collaborator/{collaboratorId}/has-interval",
                worklogAuthHandler::doesCollaboratorHaveWorkLogInterval
            )
            GET("/{intervalId}/duration", worklogDurationHandler::measureDuration)
        }
        GET("/intervals/{intervalIds}/collaborator/{collaboratorId}/has-intervals",
            worklogAuthHandler::doesCollaboratorHaveWorkLogIntervals
        )
    }

}