package lt.boldadmin.nexus.backend.test.unit.handler.worklog

import com.nhaarman.mockitokotlin2.doReturn
import lt.boldadmin.nexus.api.service.worklog.WorklogStatusService
import lt.boldadmin.nexus.api.type.entity.Project
import lt.boldadmin.nexus.backend.handler.worklog.WorklogStatusHandler
import lt.boldadmin.nexus.backend.route.Routes
import lt.boldadmin.nexus.backend.test.unit.handler.create
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.lenient
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.test.web.reactive.server.WebTestClient

@ExtendWith(MockitoExtension::class)
class WorklogStatusHandlerTest {

    @Mock
    private lateinit var worklogStatusServiceSpy: WorklogStatusService

    private lateinit var webClient: WebTestClient

    @BeforeEach
    fun `Set up`() {
        val contextStub = create()
        lenient()
            .`when`(contextStub.getBean(WorklogStatusHandler::class.java))
            .doReturn(WorklogStatusHandler(worklogStatusServiceSpy))

        webClient = WebTestClient.bindToRouterFunction(Routes(contextStub).router()).build()
    }


    @Test
    fun `Gets project of started work`() {
        val collaboratorId = "collaboratorId"
        val project = Project().apply { id = "projectId" }
        doReturn(project)
            .`when`(worklogStatusServiceSpy)
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
        doReturn(true).`when`(worklogStatusServiceSpy).hasWorkStarted(collaboratorId)

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
        doReturn(true).`when`(worklogStatusServiceSpy).hasWorkStarted(collaboratorId, projectId)

        val response = webClient.get()
            .uri("/worklog/collaborator/$collaboratorId/project/$projectId/status/has-work-started")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Boolean::class.java)
            .returnResult()

        assertTrue(response.responseBody!!)
    }
}
