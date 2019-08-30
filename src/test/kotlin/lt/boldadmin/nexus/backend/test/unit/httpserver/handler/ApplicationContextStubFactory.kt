package lt.boldadmin.nexus.backend.test.unit.httpserver.handler

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import lt.boldadmin.nexus.backend.httpserver.handler.*
import lt.boldadmin.nexus.backend.httpserver.handler.worklog.WorklogAuthHandler
import lt.boldadmin.nexus.backend.httpserver.handler.worklog.WorklogHandler
import lt.boldadmin.nexus.backend.httpserver.handler.worklog.duration.WorklogDurationHandler
import lt.boldadmin.nexus.backend.httpserver.handler.worklog.status.WorklogStartEndHandler
import lt.boldadmin.nexus.backend.httpserver.handler.worklog.status.location.WorklogLocationHandler
import lt.boldadmin.nexus.backend.httpserver.handler.worklog.status.message.WorklogMessageHandler
import org.springframework.context.support.AbstractApplicationContext

fun create() = mock<AbstractApplicationContext>().also {
    doReturn(mock<CollaboratorHandler>()).`when`(it).getBean(CollaboratorHandler::class.java)
    doReturn(mock<CountryHandler>()).`when`(it).getBean(CountryHandler::class.java)
    doReturn(mock<ProjectHandler>()).`when`(it).getBean(ProjectHandler::class.java)
    doReturn(mock<UserHandler>()).`when`(it).getBean(UserHandler::class.java)
    doReturn(mock<WorklogAuthHandler>()).`when`(it).getBean(WorklogAuthHandler::class.java)
    doReturn(mock<WorklogDurationHandler>()).`when`(it).getBean(WorklogDurationHandler::class.java)
    doReturn(mock<WorklogHandler>()).`when`(it).getBean(WorklogHandler::class.java)
    doReturn(mock<WorklogLocationHandler>()).`when`(it).getBean(WorklogLocationHandler::class.java)
    doReturn(mock<WorklogMessageHandler>()).`when`(it).getBean(WorklogMessageHandler::class.java)
    doReturn(mock<WorklogStartEndHandler>()).`when`(it).getBean(WorklogStartEndHandler::class.java)
}
