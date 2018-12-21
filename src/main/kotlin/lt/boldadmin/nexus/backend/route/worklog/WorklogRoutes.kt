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

    val worklogStartEndHandler = applicationContext.getBean<WorklogStartEndHandler>()
    val worklogHandler = applicationContext.getBean<WorklogHandler>()
    val worklogDescriptionHandler = applicationContext.getBean<WorklogDescriptionHandler>()
    val worklogMessageHandler = applicationContext.getBean<WorklogMessageHandler>()
    val worklogLocationHandler = applicationContext.getBean<WorklogLocationHandler>()
    val worklogDurationHandler = applicationContext.getBean<WorklogDurationHandler>()
    val worklogAuthHandler = applicationContext.getBean<WorklogAuthHandler>()


    accept(MediaType.APPLICATION_JSON).nest {
        "/status".nest(worklogStatusRoutes(worklogStartEndHandler, worklogMessageHandler, worklogLocationHandler))
        "/collaborator/{collaboratorId}/status".nest(
                worklogCollaboratorStatusRoutes(worklogStartEndHandler, worklogDescriptionHandler))
        GET("/collaborator/{collaboratorId}", worklogHandler::getByCollaboratorId)

        GET("/intervals/{intervalIds}/durations-sum", worklogDurationHandler::sumWorkDurations)
        GET("/intervals/{intervalIds}/collaborator/{collaboratorId}/has-intervals",
                worklogAuthHandler::doesCollaboratorHaveWorkLogIntervals)

        GET("/project/{projectId}/collaborator/{collaboratorId}/exists",
                worklogHandler::existsByProjectIdAndCollaboratorId)


        GET("/project/{projectId}", worklogHandler::getByProjectId)
        GET("/interval/{intervalId}/status/description", worklogDescriptionHandler::getDescription)
        GET("/interval/{intervalId}", worklogHandler::getIntervalEndpoints)
        POST("/interval/{intervalId}/status/description/update", worklogDescriptionHandler::updateDescription)
        GET("/duration", worklogDurationHandler::measureDuration)
        GET("/user/{userId}/has-interval", worklogAuthHandler::doesUserHaveWorkLogInterval)
        GET("/collaborator/{collaboratorId}/has-interval", worklogAuthHandler::doesCollaboratorHaveWorkLogInterval)
    }

}
