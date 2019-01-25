package lt.boldadmin.nexus.backend.test.unit.handler

import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.verify
import lt.boldadmin.nexus.api.service.StartedProjectWorkTokenService
import lt.boldadmin.nexus.api.type.entity.Project
import lt.boldadmin.nexus.backend.handler.StartedProjectWorkTokenHandler
import lt.boldadmin.nexus.backend.route.Routes
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
class StartedProjectWorkTokenHandlerTest {

    @Mock
    private lateinit var serviceSpy: StartedProjectWorkTokenService

    private val contextStub = create()


    @Before
    fun setUp() {
        val handler = StartedProjectWorkTokenHandler(serviceSpy)
        lenient()
            .`when`(contextStub.getBean(StartedProjectWorkTokenHandler::class.java))
            .doReturn(handler)
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
