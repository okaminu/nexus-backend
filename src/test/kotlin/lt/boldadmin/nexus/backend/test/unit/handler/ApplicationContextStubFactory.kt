package lt.boldadmin.nexus.backend.test.unit.handler

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import lt.boldadmin.nexus.backend.handler.*
import lt.boldadmin.nexus.backend.handler.worklog.WorklogAuthHandler
import lt.boldadmin.nexus.backend.handler.worklog.WorklogHandler
import lt.boldadmin.nexus.backend.handler.worklog.duration.WorklogDurationHandler
import lt.boldadmin.nexus.backend.handler.worklog.status.WorklogDescriptionHandler
import lt.boldadmin.nexus.backend.handler.worklog.status.WorklogStartEndHandler
import lt.boldadmin.nexus.backend.handler.worklog.status.location.WorklogLocationHandler
import lt.boldadmin.nexus.backend.handler.worklog.status.message.WorklogMessageHandler
import org.springframework.context.support.AbstractApplicationContext

fun create(): AbstractApplicationContext {
    val contextStub: AbstractApplicationContext = mock()

    doReturn(mock<CollaboratorHandler>()).`when`(contextStub).getBean(CollaboratorHandler::class.java)
    doReturn(mock<CompanyHandler>()).`when`(contextStub).getBean(CompanyHandler::class.java)
    doReturn(mock<CustomerHandler>()).`when`(contextStub).getBean(CustomerHandler::class.java)
    doReturn(mock<CountryHandler>()).`when`(contextStub).getBean(CountryHandler::class.java)
    doReturn(mock<ProjectHandler>()).`when`(contextStub).getBean(ProjectHandler::class.java)
    doReturn(mock<UserHandler>()).`when`(contextStub).getBean(UserHandler::class.java)
    doReturn(mock<WorklogAuthHandler>()).`when`(contextStub).getBean(WorklogAuthHandler::class.java)
    doReturn(mock<WorklogDescriptionHandler>()).`when`(contextStub).getBean(WorklogDescriptionHandler::class.java)
    doReturn(mock<WorklogDurationHandler>()).`when`(contextStub).getBean(WorklogDurationHandler::class.java)
    doReturn(mock<WorklogHandler>()).`when`(contextStub).getBean(WorklogHandler::class.java)
    doReturn(mock<WorklogLocationHandler>()).`when`(contextStub).getBean(WorklogLocationHandler::class.java)
    doReturn(mock<WorklogMessageHandler>()).`when`(contextStub).getBean(WorklogMessageHandler::class.java)
    doReturn(mock<WorklogStartEndHandler>()).`when`(contextStub).getBean(WorklogStartEndHandler::class.java)

    return contextStub
}