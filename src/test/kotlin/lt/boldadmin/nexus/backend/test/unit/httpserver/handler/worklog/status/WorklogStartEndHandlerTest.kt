package lt.boldadmin.nexus.backend.test.unit.httpserver.handler.worklog.status

import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.verify
import lt.boldadmin.nexus.api.service.worklog.WorklogStartEndService
import lt.boldadmin.nexus.api.type.entity.Collaborator
import lt.boldadmin.nexus.api.type.entity.Project
import lt.boldadmin.nexus.backend.httpserver.handler.worklog.status.WorklogStartEndHandler
import lt.boldadmin.nexus.backend.httpserver.route.Routes
import lt.boldadmin.nexus.backend.test.unit.httpserver.handler.create
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.lenient
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.toMono
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(MockitoJUnitRunner::class)
class WorklogStartEndHandlerTest {

    @Mock
    private lateinit var worklogStartEndServiceSpy: WorklogStartEndService

    private lateinit var webClient: WebTestClient

    @Before
    fun `Set up`() {
        val contextStub = create()
        lenient()
            .`when`(contextStub.getBean(WorklogStartEndHandler::class.java))
            .doReturn(WorklogStartEndHandler(worklogStartEndServiceSpy))

        webClient = WebTestClient.bindToRouterFunction(Routes(contextStub).router()).build()
    }


    @Test
    fun `Gets project of started work`() {
        val collaboratorId = "collaboratorId"
        val project = Project().apply { id = "projectId" }
        doReturn(project)
            .`when`(worklogStartEndServiceSpy)
            .getProjectOfStartedWork(collaboratorId)

        val response = webClient.get()
            .uri("/worklog/collaborator/$collaboratorId/status/project-of-started-work")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Project::class.java)
            .returnResult()

        assertEquals(project.id, response.responseBody!!.id)
    }

    @Test
    fun `Has work started`() {
        val collaboratorId = "collaboratorId"
        doReturn(true).`when`(worklogStartEndServiceSpy).hasWorkStarted(collaboratorId)

        val response = webClient.get()
            .uri("/worklog/collaborator/$collaboratorId/status/has-work-started")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Boolean::class.java)
            .returnResult()

        assertTrue(response.responseBody!!)
    }

    @Test
    fun `Has work started in a project`() {
        val collaboratorId = "collaboratorId"
        val projectId = "projectId"
        doReturn(true).`when`(worklogStartEndServiceSpy).hasWorkStarted(collaboratorId, projectId)

        val response = webClient.get()
            .uri("/worklog/collaborator/$collaboratorId/project/$projectId/status/has-work-started")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Boolean::class.java)
            .returnResult()

        assertTrue(response.responseBody!!)
    }

    fun `Starts work progress`() {
        val project = Project().apply { id = "projectId" }
        val collaborator = Collaborator().apply { id = "collaboratorId" }
        val projectOfCollaborator = Pair(collaborator, project)

        webClient.post()
            .uri("/worklog/status/start")
            .body(projectOfCollaborator.toMono(), projectOfCollaborator.javaClass)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .isEmpty

        argumentCaptor<Collaborator>().apply {
            assertEquals(project.id, firstValue.id)
        }
    }

    @Test
    fun `Ends work for collaborators where work time is ended`() {
        webClient.post()
            .uri("/worklog/status/end/all-started-work-on-ended-work-time")
            .exchange()
            .expectStatus()
            .isOk

        verify(worklogStartEndServiceSpy).endAllStartedWorkWhereWorkTimeEnded()
    }
}
