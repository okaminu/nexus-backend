package lt.boldadmin.nexus.backend.test.unit.handler

import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import lt.boldadmin.nexus.api.service.StartedProjectWorkTokenService
import lt.boldadmin.nexus.api.type.entity.Project
import lt.boldadmin.nexus.backend.handler.*
import lt.boldadmin.nexus.backend.handler.worklog.WorklogAuthHandler
import lt.boldadmin.nexus.backend.handler.worklog.WorklogHandler
import lt.boldadmin.nexus.backend.handler.worklog.duration.WorklogDurationHandler
import lt.boldadmin.nexus.backend.handler.worklog.status.WorklogDescriptionHandler
import lt.boldadmin.nexus.backend.handler.worklog.status.WorklogStartEndHandler
import lt.boldadmin.nexus.backend.handler.worklog.status.location.WorklogLocationHandler
import lt.boldadmin.nexus.backend.handler.worklog.status.message.WorklogMessageHandler
import lt.boldadmin.nexus.backend.route.Routes
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.context.support.AbstractApplicationContext
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.toMono
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(MockitoJUnitRunner::class)
class StartedProjectWorkTokenHandlerTest {

    @Mock
    private lateinit var contextStub: AbstractApplicationContext

    @Mock
    private lateinit var serviceSpy: StartedProjectWorkTokenService


    @Before
    fun setUp() {
        val handler = StartedProjectWorkTokenHandler(serviceSpy)

        doReturn(handler).`when`(contextStub).getBean(StartedProjectWorkTokenHandler::class.java)
        doReturn(mock<WorklogDurationHandler>()).`when`(contextStub).getBean(WorklogDurationHandler::class.java)
        doReturn(mock<WorklogLocationHandler>()).`when`(contextStub).getBean(WorklogLocationHandler::class.java)
        doReturn(mock<WorklogMessageHandler>()).`when`(contextStub).getBean(WorklogMessageHandler::class.java)
        doReturn(mock<WorklogDescriptionHandler>()).`when`(contextStub).getBean(WorklogDescriptionHandler::class.java)
        doReturn(mock<WorklogStartEndHandler>()).`when`(contextStub).getBean(WorklogStartEndHandler::class.java)
        doReturn(mock<WorklogAuthHandler>()).`when`(contextStub).getBean(WorklogAuthHandler::class.java)
        doReturn(mock<WorklogHandler>()).`when`(contextStub).getBean(WorklogHandler::class.java)
        doReturn(mock<CollaboratorHandler>()).`when`(contextStub).getBean(CollaboratorHandler::class.java)
        doReturn(mock<CompanyHandler>()).`when`(contextStub).getBean(CompanyHandler::class.java)
        doReturn(mock<CountryHandler>()).`when`(contextStub).getBean(CountryHandler::class.java)
        doReturn(mock<CustomerHandler>()).`when`(contextStub).getBean(CustomerHandler::class.java)
        doReturn(mock<ProjectHandler>()).`when`(contextStub).getBean(ProjectHandler::class.java)
        doReturn(mock<UserHandler>()).`when`(contextStub).getBean(UserHandler::class.java)
    }

    @Test
    fun `Exists by project id`() {
        val project = Project().apply { id = "someFancyId" }
        doReturn(true).`when`(serviceSpy).existsById(project.id)

        val routerFunction = Routes(contextStub).router()
        val webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build()
        val response = webTestClient.get()
                .uri("/started-project-work-token/project/${project.id}/exists")
                .exchange()
                .expectStatus()
                .isOk
                .expectBody(Boolean::class.java)
                .returnResult()

        assertTrue(response.responseBody!!)
    }

    @Test
    fun `Finds token by project id`() {
        val token = "token"
        val projectId = "projectId"
        doReturn(token).`when`(serviceSpy).findTokenById(projectId)

        val routerFunction = Routes(contextStub).router()
        val webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build()
        val response = webTestClient.get()
            .uri("/started-project-work-token/project/$projectId/token")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(String::class.java)
            .returnResult()

        assertEquals(token, response.responseBody!!)
    }

    @Test
    fun `Finds token id`() {
        val token = "token"
        val tokenId = "tokenId"
        doReturn(tokenId).`when`(serviceSpy).findIdByToken(token)

        val routerFunction = Routes(contextStub).router()
        val webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build()
        val response = webTestClient.get()
            .uri("/started-project-work-token/token/$token/id")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(String::class.java)
            .returnResult()

        assertEquals(tokenId, response.responseBody!!)
    }

    @Test
    fun `Finds project by token`() {
        val token = "token"
        val project = Project().apply { id = "projectId" }
        doReturn(project).`when`(serviceSpy).findProjectByToken(token)

        val routerFunction = Routes(contextStub).router()
        val webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build()
        val response = webTestClient.get()
            .uri("/started-project-work-token/token/$token/project")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Project::class.java)
            .returnResult()

        assertEquals(project.id, response.responseBody!!.id)
    }

    @Test
    fun `Finds working collaborator ids by token`() {
        val token = "token"
        val collaboratorIds = listOf("collaboratorId")
        doReturn(collaboratorIds).`when`(serviceSpy).findWorkingCollaboratorIdsByToken(token)

        val routerFunction = Routes(contextStub).router()
        val webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build()
        val response = webTestClient.get()
            .uri("/started-project-work-token/token/$token/collaborators/working")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(List::class.java)
            .returnResult()

        assertEquals(collaboratorIds, response.responseBody!!)
    }

    @Test
    fun `Generates dashboard link for project`() {
        val projectId = "projectId"

        val routerFunction = Routes(contextStub).router()
        val webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build()
        webTestClient.post()
            .uri("/started-project-work-token/generate-and-store")
            .body(projectId.toMono(), String::class.java)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .isEmpty

        argumentCaptor<String>().apply {
            verify(serviceSpy).generateAndStore(capture())
            assertEquals(projectId, firstValue)
        }
    }

    @Test
    fun `Removes dashboard link for project`() {
        val projectId = "projectId"

        val routerFunction = Routes(contextStub).router()
        val webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build()
        webTestClient.post()
            .uri("/started-project-work-token/delete")
            .body(projectId.toMono(), String::class.java)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .isEmpty

        argumentCaptor<String>().apply {
            verify(serviceSpy).deleteById(capture())
            assertEquals(projectId, firstValue)
        }
    }
}
