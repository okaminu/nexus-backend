package lt.boldadmin.nexus.backend.test.unit.handler

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import lt.boldadmin.nexus.backend.handler.CountryHandler
import lt.boldadmin.nexus.backend.handler.ProjectHandler
import lt.boldadmin.nexus.backend.handler.UserHandler
import lt.boldadmin.nexus.backend.handler.collaborator.CollaboratorCoordinatesHandler
import lt.boldadmin.nexus.backend.handler.collaborator.CollaboratorHandler
import lt.boldadmin.nexus.backend.handler.collaborator.WorkWeekValidatorHandler
import lt.boldadmin.nexus.backend.handler.worklog.*
import org.springframework.context.support.AbstractApplicationContext

fun create() = mock<AbstractApplicationContext>().also {
    doReturn(mock<CollaboratorHandler>()).`when`(it).getBean(CollaboratorHandler::class.java)
    doReturn(mock<WorkWeekValidatorHandler>()).`when`(it).getBean(WorkWeekValidatorHandler::class.java)
    doReturn(mock<CollaboratorCoordinatesHandler>()).`when`(it).getBean(CollaboratorCoordinatesHandler::class.java)
    doReturn(mock<CountryHandler>()).`when`(it).getBean(CountryHandler::class.java)
    doReturn(mock<ProjectHandler>()).`when`(it).getBean(ProjectHandler::class.java)
    doReturn(mock<UserHandler>()).`when`(it).getBean(UserHandler::class.java)
    doReturn(mock<WorklogAuthHandler>()).`when`(it).getBean(WorklogAuthHandler::class.java)
    doReturn(mock<WorklogDurationHandler>()).`when`(it).getBean(WorklogDurationHandler::class.java)
    doReturn(mock<WorklogHandler>()).`when`(it).getBean(WorklogHandler::class.java)
    doReturn(mock<WorklogStatusHandler>()).`when`(it).getBean(WorklogStatusHandler::class.java)
    doReturn(mock<WorklogOvertimeHandler>()).`when`(it).getBean(WorklogOvertimeHandler::class.java)
}
