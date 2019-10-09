package lt.boldadmin.nexus.backend.route.worklog

import lt.boldadmin.nexus.backend.handler.worklog.status.WorklogStartEndHandler
import lt.boldadmin.nexus.backend.handler.worklog.status.location.WorklogLocationHandler
import lt.boldadmin.nexus.backend.handler.worklog.status.message.WorklogMessageHandler
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

        "/log-work".nest {
            POST("/message", worklogMessageHandler::logWork)
            POST("/location", worklogLocationHandler::logWork)
        }
    }
}

