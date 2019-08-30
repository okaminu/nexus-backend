package lt.boldadmin.nexus.backend.httpserver.route.worklog

import lt.boldadmin.nexus.backend.httpserver.handler.worklog.status.WorklogStartEndHandler
import lt.boldadmin.nexus.backend.httpserver.handler.worklog.status.location.WorklogLocationHandler
import lt.boldadmin.nexus.backend.httpserver.handler.worklog.status.message.WorklogMessageHandler
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RouterFunctionDsl

fun worklogStatusRoutes(
    worklogStartEndHandler: WorklogStartEndHandler,
    worklogMessageHandler: WorklogMessageHandler,
    worklogLocationHandler: WorklogLocationHandler
): RouterFunctionDsl.() -> Unit = {

    accept(MediaType.APPLICATION_JSON).nest {
        POST("/start", worklogStartEndHandler::start)
        POST("/start/timestamp/{timestamp}", worklogStartEndHandler::startWithTimestamp)
        POST("/end", worklogStartEndHandler::end)
        POST("/end/timestamp/{timestamp}", worklogStartEndHandler::endWithTimestamp)
        POST("/end/all-started-work-on-ended-work-time", worklogStartEndHandler::endAllStartedWorkWhereWorkTimeEnded)

        "/log-work".nest {
            POST("/message", worklogMessageHandler::logWork)
            POST("/location", worklogLocationHandler::logWork)
        }
    }
}

