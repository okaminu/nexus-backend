package lt.boldadmin.nexus.backend.route

import lt.boldadmin.nexus.backend.handler.worklog.*
import org.springframework.beans.factory.getBean
import org.springframework.context.support.AbstractApplicationContext
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RouterFunctionDsl

fun worklogRoutes(applicationContext: AbstractApplicationContext): RouterFunctionDsl.() -> Unit = {

    val worklogAuthHandler: WorklogAuthHandler = applicationContext.getBean()
    val worklogDurationHandler: WorklogDurationHandler = applicationContext.getBean()
    val worklogHandler: WorklogHandler = applicationContext.getBean()
    val worklogStatusHandler: WorklogStatusHandler = applicationContext.getBean()

    accept(MediaType.APPLICATION_JSON).nest {
        POST("/save", worklogHandler::save)
        POST(
            "/status/end/all-started-work-on-ended-work-time",
            worklogStatusHandler::endAllStartedWorkWhereWorkTimeEnded
        )

        "/collaborator".nest {
            GET("/{collaboratorId}/interval-ids", worklogHandler::getIntervalIdsByCollaboratorId)
            GET(
                "/{collaboratorId}/start/{startDate}/end/{endDate}/interval-ids",
                worklogHandler::getIntervalIdsByCollaboratorIdAndDateRange
            )
            GET("/{collaboratorId}/status/has-work-started", worklogStatusHandler::hasWorkStarted)
            GET(
                "/{collaboratorId}/project/{projectId}/status/has-work-started",
                worklogStatusHandler::hasWorkStartedInProject
            )
            GET("/{collaboratorId}/status/project-of-started-work", worklogStatusHandler::getProjectOfStartedWork)
            GET("/{collaboratorId}/durations-sum", worklogDurationHandler::sumWorkDurationsByCollaboratorId)
            GET(
                "/{collaboratorId}/start/{startDate}/end/{endDate}/durations-sum",
                worklogDurationHandler::sumWorkDurationsByCollaboratorIdAndDateRange
            )
        }
        "/project".nest {
            GET("/{projectId}/interval-ids", worklogHandler::getIntervalIdsByProjectId)
            GET(
                "/{projectId}/start/{startDate}/end/{endDate}/interval-ids",
                worklogHandler::getIntervalIdsByProjectIdAndDateRange
            )
            GET("/{projectId}/durations-sum", worklogDurationHandler::sumWorkDurationsByProjectId)
            GET(
                "/{projectId}/start/{startDate}/end/{endDate}/durations-sum",
                worklogDurationHandler::sumWorkDurationsByProjectIdAndDateRange
            )
        }
        "/interval".nest {
            GET("/{intervalId}/endpoints", worklogHandler::getIntervalEndpoints)
            GET("/{intervalId}/user/{userId}/has-interval", worklogAuthHandler::doesUserHaveWorkLogInterval)
            GET(
                "/{intervalId}/collaborator/{collaboratorId}/has-interval",
                worklogAuthHandler::doesCollaboratorHaveWorkLogInterval
            )
            GET("/{intervalId}/duration", worklogDurationHandler::measureDuration)
        }
        GET(
            "/intervals/{intervalIds}/collaborator/{collaboratorId}/has-intervals",
            worklogAuthHandler::doesCollaboratorHaveWorkLogIntervals
        )
    }

}